package eu.tutorials.propertyapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddPropertyScreen(navController: NavController, viewModel: PropertyViewModel) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var marketValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") }
        )
        OutlinedTextField(
            value = floor,
            onValueChange = { if (it.all { char -> char.isDigit() }) floor = it },
            label = { Text("Floor") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = area,
            onValueChange = { if (it.all { char -> char.isDigit() }) area = it },
            label = { Text("Area") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = marketValue,
            onValueChange = { if (it.all { char -> char.isDigit() }) marketValue = it },
            label = { Text("Market Value") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                if (name.isNotEmpty() && address.isNotEmpty() && floor.isNotEmpty() && area.isNotEmpty() && marketValue.isNotEmpty()) {
                    val newProperty = Property(
                        id = viewModel.properties.size.toString(),
                        name = name,
                        address = address,
                        floor = floor,
                        area = area,
                        marketValue = marketValue
                    )
                    viewModel.addProperty(newProperty)
                    navController.navigate("firstScreen") {
                        popUpTo("firstScreen") { inclusive = true }
                    }
                }
            }) {
                Text("Add")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { navController.navigate("firstScreen") }) {
                Text("Cancel")
            }
        }
    }
}
