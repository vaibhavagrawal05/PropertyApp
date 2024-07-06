package eu.tutorials.propertyapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import com.itextpdf.layout.element.Image as PdfImage

@Composable
fun PropertyDetailsScreen(navController: NavController, property: Property) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri: Uri? ->
//            uri?.let {
//                // Handle the selected image URI
//                handleImageUri(context, it)
//            }
//        }
//    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Name: ${property.name}", style = MaterialTheme.typography.bodyLarge)
        Text("Address: ${property.address}", style = MaterialTheme.typography.bodyLarge)
        Text("Floor: ${property.floor}", style = MaterialTheme.typography.bodyLarge)
        Text("Area: ${property.area}", style = MaterialTheme.typography.bodyLarge)
        Text("Market Value: ${property.marketValue}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("addRoomScreen/${property.id}") }) {
            Text("List of Inspections")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(property.rooms) { room ->
                RoomItem(room)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                generatePdf(context, property)
            }
        }) {
            Text("Generate PDF")
        }
        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            galleryLauncher.launch("image/*")
//        }) {
//            Text("Choose from Gallery")
//        }
    }
}

@Composable
fun RoomItem(room: Room) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Inspection Date: ${room.date}", style = MaterialTheme.typography.bodyLarge) // Display inspection date
        Spacer(modifier = Modifier.height(8.dp))
        Text("Description: ${room.description}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            room.images.forEach { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

//fun handleImageUri(context: Context, uri: Uri) {
//    // Do something with the selected image URI
//    Log.d("PropertyDetailsScreen", "Selected image URI: $uri")
//}

