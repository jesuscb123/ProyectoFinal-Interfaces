package dam2.jetpack.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dam2.jetpack.proyectofinal.auth.presentation.screen.AuthScreen
import dam2.jetpack.proyectofinal.auth.presentation.screen.RegisterScreen
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel
import dam2.jetpack.proyectofinal.ui.theme.ProyectoFinalTheme
import dam2.jetpack.proyectofinal.user.presentation.screen.HomeScreen
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IniciarApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarApp(){

    val navController = rememberNavController()

    ProyectoFinalTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                Text("Proyecto final")
            })
        }){ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "register",
                modifier = Modifier.padding(innerPadding
                )){
                composable("auth") { AuthScreen(navcontroller = navController) }
                composable ( "register" ) { RegisterScreen(navController = navController) }
                composable ("home") { HomeScreen() }

            }

        }
    }

}

