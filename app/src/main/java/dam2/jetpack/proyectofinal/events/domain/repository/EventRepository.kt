package dam2.jetpack.proyectofinal.events.domain.repository

import dam2.jetpack.proyectofinal.events.domain.model.Event
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define el contrato para el repositorio de eventos.
 *
 * Esta abstracción permite desacoplar los casos de uso y la lógica de negocio de la
 * implementación concreta de la fuente de datos (local o remota).
 */
interface EventRepository {
    /**
     * Obtiene un flujo con la lista de todos los eventos.
     *
     * @return Un [Flow] que emite la lista de todos los [Event].
     */
    suspend fun getAllEvents(): Flow<List<Event>>

    /**
     * Obtiene un evento específico por su ID.
     *
     * @param eventId El ID del evento a buscar.
     * @return El [Event] correspondiente.
     */
    suspend fun getById(eventId: Long): Event

    /**
     * Crea un nuevo evento en la fuente de datos.
     *
     * @param event El [Event] a crear.
     */
    suspend fun createEvent (event: Event)

    /**
     * Elimina un evento por su ID.
     *
     * @param eventId El ID del evento a eliminar.
     * @return El número de filas afectadas.
     */
    suspend fun deleteEvent (eventId: Long): Int

    /**
     * Marca un evento como aceptado, actualizando su estado.
     *
     * @param event El [Event] que se va a aceptar/actualizar.
     */
    suspend fun acceptEvent(event: Event)

    /**
     * Obtiene un flujo de eventos que han sido aceptados por un usuario específico.
     *
     * @param userAccept El identificador del usuario.
     * @return Un [Flow] que emite la lista de eventos aceptados por ese usuario.
     */
    fun getEventsUser(userAccept: String): Flow<List<Event>>

    /**
     * Obtiene un flujo de eventos que fueron creados por un usuario específico.
     *
     * @param userId El identificador del usuario creador.
     * @return Un [Flow] que emite la lista de eventos creados por ese usuario.
     */
    fun getEventsUserCreate(userId: String): Flow<List<Event>>
}

