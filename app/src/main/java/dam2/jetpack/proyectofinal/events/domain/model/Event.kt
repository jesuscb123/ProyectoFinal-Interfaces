package dam2.jetpack.proyectofinal.events.domain.model

import dam2.jetpack.proyectofinal.user.domain.model.User
import java.util.Date

/**
 * Representa el modelo de dominio de un evento.
 *
 * Esta clase de datos es la representación principal de un evento utilizada en la lógica
 * de negocio y la capa de presentación (UI) de la aplicación.
 *
 * @property eventId El ID único del evento en la base de datos local. Puede ser nulo si el evento aún no ha sido guardado.
 * @property userId El identificador del usuario que creó el evento.
 * @property creatorUid El UID de Firebase del usuario creador.
 * @property tituloEvento El título o nombre del evento.
 * @property descripcionEvento Una descripción más detallada del evento.
 * @property categoria La categoría a la que pertenece el evento (e.g., [Category.COMUNIDAD], [Category.PERSONAL]).
 * @property fechaCreacion La fecha y hora en que el evento fue creado.
 * @property resuelto Un booleano que indica si el evento ha sido marcado como resuelto o completado.
 * @property userAccept El identificador del usuario que ha aceptado o ha sido asignado para resolver el evento. Es nulo si nadie lo ha aceptado.
 * @property userAcceptUid El UID de Firebase del usuario que ha aceptado el evento. Es nulo si nadie lo ha aceptado.
 */
data class Event(
    val eventId: Long?= null,
    val userId: String,
    val creatorUid: String,
    val tituloEvento: String,
    val descripcionEvento: String,
    val categoria: Category,
    val fechaCreacion: Date,
    val resuelto: Boolean = false,
    val userAccept: String?,
    val userAcceptUid: String?
)
