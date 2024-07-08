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
fun EditPropertyScreen(navController: NavController, viewModel: PropertyViewModel) {
    val property = viewModel.properties.firstOrNull { it.isEditing } ?: return

    var name by remember { mutableStateOf(property.name) }
    var address by remember { mutableStateOf(property.address) }
    var floor by remember { mutableStateOf(property.floor) }
    var area by remember { mutableStateOf(property.area) }
    var marketValue by remember { mutableStateOf(property.marketValue) }

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
                    val updatedProperty = property.copy(
                        name = name,
                        address = address,
                        floor = floor,
                        area = area,
                        marketValue = marketValue,
                        isEditing = false
                    )
                    viewModel.editProperty(property, updatedProperty)
                    navController.navigate("firstScreen") {
                        popUpTo("firstScreen") { inclusive = true }
                    }
                }
            }) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { navController.navigate("firstScreen") }) {
                Text("Cancel")
            }
        }
    }
}
