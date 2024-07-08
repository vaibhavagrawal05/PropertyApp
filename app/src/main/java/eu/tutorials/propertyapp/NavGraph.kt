package eu.tutorials.propertyapp

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.tutorials.propertyapp.database.PropertyViewModelFactory
import eu.tutorials.propertyapp.loginscreens.LoginPage
import eu.tutorials.propertyapp.loginscreens.SignUpPage
import eu.tutorials.propertyapp.loginscreens.FirstPage
import eu.tutorials.propertyapp.loginscreens.SignOutPage
import eu.tutorials.propertyapp.viewmodel.AuthViewModel

@Composable
fun MyApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext as Application
    val viewModel: PropertyViewModel = viewModel(factory = PropertyViewModelFactory(context))

    NavHost(navController = navController, startDestination = "firstPage") {
        composable("firstPage") {
            FirstPage(navController = navController, authViewModel = authViewModel)
        }
        composable("login") {
            LoginPage(navController = navController, authViewModel = authViewModel)
        }
        composable("signup") {
            SignUpPage(navController = navController, authViewModel = authViewModel)
        }
        composable("firstScreen") {
            FirstScreen(navController = navController, viewModel = viewModel)
        }

        composable("signout") {
            SignOutPage(navController = navController, authViewModel = authViewModel)
        }

        composable("addPropertyScreen") {
            AddPropertyScreen(navController = navController, viewModel = viewModel)
        }
        composable("editPropertyScreen") {
            EditPropertyScreen(navController = navController, viewModel = viewModel)
        }
        composable("propertyDetailsScreen/{propertyId}") { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")
            val property = viewModel.properties.firstOrNull { it.id == propertyId }
            if (property != null) {
                PropertyDetailsScreen(navController = navController, property = property)
            }
        }
        composable("addRoomScreen/{propertyId}") { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")
            if (propertyId != null) {
                AddRoomScreen(navController = navController, viewModel = viewModel, propertyId = propertyId)
            }

        }
    }
}