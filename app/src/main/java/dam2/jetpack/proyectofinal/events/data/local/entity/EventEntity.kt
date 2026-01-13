package dam2.jetpack.proyectofinal.events.data.local.entity

import androidx.room.PrimaryKey
import dam2.jetpack.proyectofinal.events.domain.model.Category

data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: String,
    val tituloEvento: String,
    val descripcionEvento: String,
    val categoria: Category,
    val fechaCreacion: String,
    val resuelto: Boolean
)
