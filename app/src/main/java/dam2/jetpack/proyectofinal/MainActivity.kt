package dam2.jetpack.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dam2.jetpack.proyectofinal.auth.presentation.screen.AuthScreen
import dam2.jetpack.proyectofinal.auth.presentation.screen.RegisterScreen
import dam2.jetpack.proyectofinal.events.presentation.screen.CreateEvent
import dam2.jetpack.proyectofinal.ui.theme.ProyectoFinalTheme
import dam2.jetpack.proyectofinal.user.presentation.screen.HomeScreen

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
    val emailId = FirebaseAuth.getInstance().currentUser?.email
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ProyectoFinalTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                val currentRoute = navBackStackEntry?.destination?.route
                if (currentRoute == "home") {
                    FloatingActionButton(onClick = {
                       navController.navigate("createEvent")
                    }) {
                        Text("add")
                    }
                }
            }
        ){ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "register",
                modifier = Modifier.padding(innerPadding
                )){
                composable("auth") { AuthScreen(navcontroller = navController) }
                composable ( "register" ) { RegisterScreen(navController = navController) }
                composable ("home") { HomeScreen() }
                composable ("createEvent") {CreateEvent(
                    emailId, navController = navController)}
            }
        }
    }

}

