package dam2.jetpack.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import dam2.jetpack.proyectofinal.user.presentation.screen.EventsUserScreen
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
fun IniciarApp(
    userViewModel: UserViewModel = hiltViewModel()
){
    val navController = rememberNavController()
    val emailId = FirebaseAuth.getInstance().currentUser?.email
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userState by userViewModel.uiState.collectAsState()
    val canNavigateBack = navController.previousBackStackEntry != null

    LaunchedEffect(FirebaseAuth.getInstance().currentUser?.uid) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            userViewModel.getUserByFirebaseUid(it)
        }
    }

    ProyectoFinalTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (currentRoute == "home") {
                    FloatingActionButton(onClick = {
                       navController.navigate("createEvent")
                    }) {
                        Text("add")
                    }
                }
            },
            topBar = {
                TopAppBar(title = {
                    if (currentRoute != "auth"  && currentRoute != "register" ) {
                        Column{
                            Text(
                                text = "Hola,",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = userState.user?.email ?: "Bienvenido",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                },
                    colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        if (canNavigateBack){
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                    },
                    actions = {
                        if (currentRoute == "home") {
                            IconButton(onClick = { navController.navigate("myEvents") }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Mis Eventos",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    })
            }
        ){ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "auth",
                modifier = Modifier.padding(innerPadding
                )){
                composable("auth") { AuthScreen(navcontroller = navController) }
                composable ( "register" ) { RegisterScreen(navController = navController) }
                composable ("home") { HomeScreen(onMyEventsClick = {
                    navController.navigate("myEvents")
                }) }
                composable ("createEvent") {CreateEvent(
                    emailId, navController = navController)}

                composable("myEvents") {
                    EventsUserScreen()
                }

            }
        }
    }

}

