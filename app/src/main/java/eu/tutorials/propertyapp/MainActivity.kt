package eu.tutorials.propertyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import eu.tutorials.propertyapp.ui.theme.PropertyAppTheme
import eu.tutorials.propertyapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private lateinit var propertyViewModel: PropertyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            propertyViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(PropertyViewModel::class.java)
            propertyViewModel.fetchAndStoreProperties("your_api_key")
            PropertyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = viewModel()
                    MyApp(authViewModel)
                }
            }
        }
    }
}
