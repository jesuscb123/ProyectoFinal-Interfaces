package dam2.jetpack.proyectofinal.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dam2.jetpack.proyectofinal.events.domain.model.Category
import java.util.Date

/**
 * Representa la entidad de un evento en la base de datos local (Room).
 *
 * Esta clase de datos define la estructura de la tabla `events`, incluyendo
 * todas las columnas que almacenan la información de un evento.
 *
 * @property eventId La clave primaria autoincremental del evento en la base de datos local.
 * @property userId El identificador del usuario que creó el evento.
 * @property creatorUid El UID de Firebase del creador del evento.
 * @property tituloEvento El título del evento.
 * @property descripcionEvento La descripción detallada del evento.
 * @property categoria La categoría del evento, almacenada usando un [CategoryConverter].
 * @property fechaCreacion La fecha en que el evento fue creado.
 * @property resuelto Un booleano que indica si el evento ha sido resuelto o cerrado.
 * @property userAccept El identificador del usuario que ha aceptado/ha sido asignado a este evento. Puede ser nulo.
 * @property userAcceptUid El UID de Firebase del usuario que ha aceptado/ha sido asignado a este evento. Puede ser nulo.
 */
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Long? = null,
    val userId: String,
    val creatorUid: String,
    val tituloEvento: String,
    val descripcionEvento: String,
    val categoria: Category,
    val fechaCreacion: Date,
    val resuelto: Boolean,
    val userAccept: String?,
    val userAcceptUid: String?
)
