package dam2.jetpack.proyectofinal.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dam2.jetpack.proyectofinal.events.domain.model.Category
import java.util.Date

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Long,
    val userId: String,
    val tituloEvento: String,
    val descripcionEvento: String,
    val categoria: Category,
    val fechaCreacion: Date,
    val resuelto: Boolean
)
