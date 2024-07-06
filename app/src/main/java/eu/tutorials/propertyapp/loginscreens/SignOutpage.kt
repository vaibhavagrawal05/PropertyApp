package eu.tutorials.propertyapp.loginscreens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import eu.tutorials.propertyapp.viewmodel.AuthViewModel


fun SignOutPage(navController: NavController, authViewModel: AuthViewModel){
    authViewModel.signout();
    navController.navigate("login")
}