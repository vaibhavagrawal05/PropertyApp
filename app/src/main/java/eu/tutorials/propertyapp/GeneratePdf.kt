package eu.tutorials.propertyapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.content.FileProvider
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import eu.tutorials.propertyapp.Property
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

fun generatePdf(context: Context, property: Property) {
    val pdfPath = context.getExternalFilesDir(null)?.absolutePath + "/generated.pdf"

    try {
        val pdfWriter = PdfWriter(pdfPath)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Add property details
        document.add(Paragraph("Name: ${property.name}"))
        document.add(Paragraph("Address: ${property.address}"))
        document.add(Paragraph("Floor: ${property.floor}"))
        document.add(Paragraph("Area: ${property.area}"))
        document.add(Paragraph("Market Value: ${property.marketValue}"))

        // Add room details with images
        property.rooms.forEach { room ->
            document.add(Paragraph("Inspection Date: ${room.date}"))
            document.add(Paragraph("Description: ${room.description}"))

            room.images.forEach { imageUri ->
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    if (bitmap != null) {
                        // Resize the bitmap to prevent memory issues
                        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true)
                        val stream = ByteArrayOutputStream()
                        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val imageBytes = stream.toByteArray()
                        val image = Image(ImageDataFactory.create(imageBytes))
                        document.add(image)
                    } else {
                        document.add(Paragraph("Image not available"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    document.add(Paragraph("Error loading image: ${e.message}"))
                    Log.e("generatePdf", "Error loading image", e)
                }
            }
        }

        document.close()
        openGeneratedPdf(context, pdfPath)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("generatePdf", "Error generating PDF", e)
        // Show an error message or toast
    }
}

fun openGeneratedPdf(context: Context, pdfPath: String) {
    try {
        val file = File(pdfPath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("openGeneratedPdf", "Error opening PDF", e)
        // Show an error message or toast
    }
}

