package dam2.jetpack.proyectofinal.events.data.repository

import dam2.jetpack.proyectofinal.events.data.local.dao.EventDao
import dam2.jetpack.proyectofinal.events.data.mapper.toDomain
import dam2.jetpack.proyectofinal.events.data.mapper.toEntity
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
) : EventRepository {
    override suspend fun getAllEvents(): Flow<List<Event>> {
        val eventList = eventDao.getAllEvents()
        return eventList.map {list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getById(eventId: Long): Event {
        val eventEntity = eventDao.getEventById(eventId) ?: error("Evento no encontrado")
        return eventEntity.toDomain()
    }

    override suspend fun createEvent(event: Event) {
        eventDao.insertOrReplaceEvent(event.toEntity())
    }

    override suspend fun deleteEvent(eventId: Long): Int {
        return eventDao.deleteEventById(eventId)
    }

    override suspend fun acceptEvent(event: Event, userEmail: String?){
        val updatedEvent = event.copy(userAccept = userEmail)
        eventDao.insertOrReplaceEvent(updatedEvent.toEntity())
    }

    override suspend fun getEventsUser(userAccept: String): Flow<List<Event>> {
        val eventList = eventDao.getAllEvents()
        return eventList.map {list ->
            list.map { it.toDomain() }
        }
    }
}