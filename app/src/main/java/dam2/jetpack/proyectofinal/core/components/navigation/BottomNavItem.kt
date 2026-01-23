package dam2.jetpack.proyectofinal.core.components.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Representa un único elemento en la barra de navegación inferior (Bottom Navigation Bar).
 *
 * Esta clase de datos contiene la información necesaria para renderizar un ítem
 * en la barra de navegación, incluyendo su ruta de destino, etiqueta de texto e icono.
 *
 * @property route La ruta de navegación asociada al ítem (ej: "home", "profile").
 * @property label La etiqueta de texto que se muestra debajo del icono.
 * @property icon El [ImageVector] que se muestra para este ítem.
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
