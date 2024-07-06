package eu.tutorials.propertyapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FirstScreen(navController: NavController, viewModel: PropertyViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("addPropertyScreen") }) {
            Text("Add Property")
        }
        Button(onClick = { navController.navigate("signout") }) {
            Text("Sign Out")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(viewModel.properties) { property ->
                PropertyItem(
                    property = property,
                    onEditClick = {
                        viewModel.startEditing(property)
                        navController.navigate("editPropertyScreen")
                    },
                    onDeleteClick = {
                        viewModel.removeProperty(property)
                    },
                    onDetailsClick = {
                        navController.navigate("propertyDetailsScreen/${property.id}")
                    }
                )
            }
        }
    }
}


@Composable
fun PropertyItem(
    property: Property,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDetailsClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Name: ${property.name}")
            Text("Address: ${property.address}")
            Text("Floor: ${property.floor}")
            Text("Area: ${property.area}")
            Text("Market Value: ${property.marketValue}")
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onEditClick) {
                    Text("Edit")
                }
                TextButton(onClick = onDeleteClick) {
                    Text("Delete")
                }
                TextButton(onClick = onDetailsClick) {
                    Text("Actions")
                }
            }
        }
    }
}
