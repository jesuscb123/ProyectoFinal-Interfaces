package dam2.jetpack.proyectofinal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dam2.jetpack.proyectofinal.auth.presentation.screen.AuthScreen
import dam2.jetpack.proyectofinal.auth.presentation.screen.RegisterScreen
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel
import dam2.jetpack.proyectofinal.core.components.navigation.BottomNavItem
import dam2.jetpack.proyectofinal.events.presentation.screen.CreateEvent
import dam2.jetpack.proyectofinal.ui.theme.ProyectoFinalTheme
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.presentation.screen.AdminScreen
import dam2.jetpack.proyectofinal.user.presentation.screen.EventsUserCreateScreen
import dam2.jetpack.proyectofinal.user.presentation.screen.EventsUserScreen
import dam2.jetpack.proyectofinal.user.presentation.screen.HomeScreen
import dam2.jetpack.proyectofinal.user.presentation.screen.ViewPointsUserScreen
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
private fun bottomItems(isAdmin: Boolean): List<BottomNavItem> {
    val base = listOf(
        BottomNavItem("home", "Inicio", Icons.Default.Home),
        BottomNavItem("myEventsAccepted", "Aceptados", Icons.Default.Checklist),
        BottomNavItem("myCreatedEvents", "Mis eventos", Icons.Default.AccountCircle),
        BottomNavItem("pointsUser", "Puntos", Icons.Default.WorkspacePremium),
    )
    return if (isAdmin) base + BottomNavItem("adminScreen", "Admin", Icons.Default.Settings) else base
}
@Composable
private fun AppBottomBar(
    currentRoute: String?,
    isAdmin: Boolean,
    onNavigate: (String) -> Unit
) {
    val items = remember(isAdmin) { bottomItems(isAdmin) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarApp(
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val emailId = FirebaseAuth.getInstance().currentUser?.email

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isChat = currentRoute?.startsWith("chat") == true
    val chatRecipientEmail = navBackStackEntry?.arguments?.getString("recipientEmail")

    val canNavigateBack = navController.previousBackStackEntry != null

    val authState by authViewModel.uiState.collectAsState()
    val userState by userViewModel.uiState.collectAsState()

    val isAdmin = userState.user?.rol == Rol.ADMIN
    Log.d("USERS ADMIN", isAdmin.toString())

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home" else "auth"

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            userViewModel.getUserByFirebaseUid(it)
        }
    }

    LaunchedEffect(key1 = authState.isAuthenticated) {
        if (!authState.isAuthenticated && FirebaseAuth.getInstance().currentUser == null) {
            navController.navigate("auth") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    ProyectoFinalTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),

            bottomBar = {
                val showBottomBar = currentRoute in listOf(
                    "home",
                    "myEventsAccepted",
                    "myCreatedEvents",
                    "pointsUser",
                    "adminScreen"
                )

                if (showBottomBar) {
                    AppBottomBar(
                        currentRoute = currentRoute,
                        isAdmin = isAdmin,
                        onNavigate = { route ->
                            if (route != currentRoute) {
                                navController.navigate(route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            },

            floatingActionButton = {
                if (currentRoute == "home") {
                    FloatingActionButton(
                        onClick = { navController.navigate("createEvent") }
                    ) {
                        Text("add")
                    }
                }
            },

            topBar = {
                TopAppBar(
                    title = {
                        when {
                            isChat -> {
                                Text(chatRecipientEmail?.substringBefore('@') ?: "Chat")
                            }

                            currentRoute == "home" -> {
                                Column {
                                    Text(
                                        text = "Hola,",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = FirebaseAuth.getInstance().currentUser?.email ?: "Bienvenido",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }

                            currentRoute == "myEventsAccepted" -> Text("Eventos aceptados")
                            currentRoute == "myCreatedEvents" -> Text("Mis eventos")
                            currentRoute == "createEvent" -> Text("Crear evento")
                            currentRoute == "pointsUser" -> Text("Mis puntos")
                            currentRoute == "adminScreen" -> Text("Admin")
                            currentRoute == "auth" -> Text("Iniciar sesión")
                            currentRoute == "register" -> Text("Registro")
                            else -> Text("")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        if (currentRoute == "home") {
                            IconButton(onClick = {
                                authViewModel.logOut()
                                navController.navigate("auth")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Cerrar Sesión"
                                )
                            }
                        } else if (canNavigateBack) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                    },
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("auth") { AuthScreen(navcontroller = navController) }
                composable("register") { RegisterScreen(navController = navController) }

                composable("home") {
                    HomeScreen(navController)
                }

                composable("createEvent") {
                    CreateEvent(emailId, navController = navController)
                }

                composable("myEventsAccepted") { EventsUserScreen(navController = navController) }
                composable("myCreatedEvents") { EventsUserCreateScreen() }
                composable("pointsUser") { ViewPointsUserScreen() }

                composable("adminScreen") {
                    LaunchedEffect(isAdmin) {
                        if (!isAdmin) {
                            navController.navigate("home") {
                                popUpTo("adminScreen") { inclusive = true }
                            }
                        }
                    }
                    if (isAdmin) {
                        AdminScreen()
                    }
                }

                composable(
                    route = "chat?eventId={eventId}&recipientUid={recipientUid}&recipientEmail={recipientEmail}",
                    arguments = listOf(
                        navArgument("eventId") { type = NavType.StringType },
                        navArgument("recipientUid") { type = NavType.StringType },
                        navArgument("recipientEmail") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId")
                    val recipientUid = backStackEntry.arguments?.getString("recipientUid")
                    val recipientEmail = backStackEntry.arguments?.getString("recipientEmail")

                    if (eventId != null && recipientUid != null && recipientEmail != null) {
                        dam2.jetpack.proyectofinal.chat.presentation.screen.ChatScreen(
                            eventId = eventId,
                            recipientUid = recipientUid,
                            recipientEmail = recipientEmail,
                            navController = navController
                        )
                    } else {
                        navController.navigateUp()
                    }
                }

            }
        }
    }
}

@Composable
fun TopBarCardTitle(
    title: String,
    subtitle: String? = null
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1
            )
        }
    }
}

