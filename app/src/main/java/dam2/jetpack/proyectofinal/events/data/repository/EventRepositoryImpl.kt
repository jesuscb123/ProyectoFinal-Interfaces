package dam2.jetpack.proyectofinal.events.data.repository

import dam2.jetpack.proyectofinal.events.data.local.dao.EventDao
import dam2.jetpack.proyectofinal.events.data.mapper.toDomain
import dam2.jetpack.proyectofinal.events.data.mapper.toEntity
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación del repositorio de eventos [EventRepository].
 *
 * Esta clase se encarga de la lógica de acceso a datos para los eventos, interactuando
 * directamente con el [EventDao] para operaciones en la base de datos local.
 * Utiliza funciones de mapeo para convertir entre entidades de base de datos y modelos de dominio.
 *
 * @param eventDao El Data Access Object para la entidad de eventos, inyectado por Hilt.
 */
class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
) : EventRepository {
    /**
     * Obtiene todos los eventos de la base de datos como un [Flow].
     *
     * @return Un [Flow] que emite una lista de [Event] del dominio.
     */
    override suspend fun getAllEvents(): Flow<List<Event>> {
        val eventList = eventDao.getAllEvents()
        return eventList.map {list ->
            list.map { it.toDomain() }
        }
    }

    /**
     * Obtiene un evento específico por su ID.
     *
     * @param eventId El ID del evento a buscar.
     * @return El objeto [Event] correspondiente.
     * @throws IllegalStateException si el evento no se encuentra.
     */
    override suspend fun getById(eventId: Long): Event {
        val eventEntity = eventDao.getEventById(eventId) ?: error("Evento no encontrado")
        return eventEntity.toDomain()
    }

    /**
     * Crea un nuevo evento en la base de datos.
     *
     * @param event El objeto [Event] del dominio a crear.
     */
    override suspend fun createEvent(event: Event) {
        eventDao.insertOrReplaceEvent(event.toEntity())
    }

    /**
     * Elimina un evento por su ID.
     *
     * @param eventId El ID del evento a eliminar.
     * @return El número de filas eliminadas.
     */
    override suspend fun deleteEvent(eventId: Long): Int {
        return eventDao.deleteEventById(eventId)
    }

    /**
     * Marca un evento como aceptado, actualizándolo en la base de datos.
     *
     * @param event El objeto [Event] con los datos actualizados para guardar.
     */
    override suspend fun acceptEvent(event: Event){
        eventDao.insertOrReplaceEvent(event.toEntity())
    }

    /**
     * Obtiene todos los eventos aceptados por un usuario específico.
     *
     * @param userAccept El identificador del usuario.
     * @return Un [Flow] que emite una lista de [Event] del dominio.
     */
    override fun getEventsUser(userAccept: String): Flow<List<Event>> {
        val eventList = eventDao.getEventsUser(userAccept)
        return eventList.map {list ->
            list.map { it.toDomain() }
        }
    }

    /**
     * Obtiene todos los eventos creados por un usuario específico.
     *
     * @param userId El identificador del usuario creador.
     * @return Un [Flow] que emite una lista de [Event] del dominio.
     */
    override fun getEventsUserCreate(userId: String): Flow<List<Event>> {
        val eventList = eventDao.getEventsUserCreate(userId)
        return eventList.map {list ->
            list.map { it.toDomain() }
        }
    }
}
