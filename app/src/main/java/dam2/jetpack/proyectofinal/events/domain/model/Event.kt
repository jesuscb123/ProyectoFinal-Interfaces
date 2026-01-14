package dam2.jetpack.proyectofinal.events.domain.model

import dam2.jetpack.proyectofinal.user.domain.model.User
import java.util.Date

data class Event(
    val eventId: Long?,
    val userId: String,
    val tituloEvento: String,
    val descripcionEvento: String,
    val categoria: Category,
    val fechaCreacion: Date,
    val resuelto: Boolean = false
)