package dam2.jetpack.proyectofinal.events.domain.repository

import dam2.jetpack.proyectofinal.events.domain.model.Event

interface EventRepository {
    suspend fun getAllEvents(): List<Event>
    suspend fun getById(eventId: Long): Event?
    suspend fun createEvent (event: Event)
    suspend fun deleteEvent (eventId: Long): Int
}