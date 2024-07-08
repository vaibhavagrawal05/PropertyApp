package eu.tutorials.propertyapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File

private const val TAG = "AddRoomScreen"

@Composable
fun AddRoomScreen(navController: NavController, viewModel: PropertyViewModel, propertyId: String) {
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var images by remember { mutableStateOf(listOf<Uri>()) }
    var inspectionDate by remember { mutableStateOf(TextFieldValue("")) } // State for inspection date
    val context = LocalContext.current

    // Local variable for currentImageUri
    var currentImageUri by remember { mutableStateOf<Uri?>(null) }

    // ActivityResultLauncher for camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentImageUri?.let { imageUri ->
                images = images + imageUri
                Log.d(TAG, "Image captured: $imageUri")
            }
        }
    }

    // ActivityResultLauncher for gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                images = uris
                Log.d(TAG, "Images selected: $uris")
                Toast.makeText(context, "Images selected: $uris", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "No images selected")
                Toast.makeText(context, "No images selected", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Request permissions for camera
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val imageFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
            currentImageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
            currentImageUri?.let { cameraLauncher.launch(it) }
        } else {
            Log.d(TAG, "Camera permission denied")
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Request permissions for gallery
    val requestGalleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Gallery permission granted")
            Toast.makeText(context, "Gallery permission granted", Toast.LENGTH_SHORT).show()
            galleryLauncher.launch("image/*")
        } else {
            Log.d(TAG, "Gallery permission denied")
            Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_SHORT).show()
            // You can add logic to show an explanation to the user here
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = inspectionDate,
            onValueChange = { inspectionDate = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (inspectionDate.text.isEmpty()) {
                        Text("Inspection Date", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (description.text.isEmpty()) {
                        Text("Description", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Take Photos")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Gallery permission already granted")
                    Toast.makeText(context, "Gallery permission already granted", Toast.LENGTH_SHORT).show()
                    galleryLauncher.launch("image/*")
                } else {
                    Log.d(TAG, "Requesting gallery permission")
                    Toast.makeText(context, "Requesting gallery permission", Toast.LENGTH_SHORT).show()
                    requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }) {
                Text("Pick Images from Gallery")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        images.forEach { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val room = Room(description.text, images, inspectionDate.text) // Include inspection date
            viewModel.addRoomToProperty(propertyId, room)
            navController.popBackStack()
        }) {
            Text("Add this room")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}