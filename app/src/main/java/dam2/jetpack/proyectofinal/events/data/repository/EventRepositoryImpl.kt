package dam2.jetpack.proyectofinal.events.data.repository

import dam2.jetpack.proyectofinal.events.data.local.dao.EventDao
import dam2.jetpack.proyectofinal.events.data.mapper.toDomain
import dam2.jetpack.proyectofinal.events.data.mapper.toEntity
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
) : EventRepository {
    override suspend fun getAllEvents(): List<Event> {
        val eventEntity = eventDao.getAllEvents()
        return eventEntity.map { it.toDomain() }
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
}