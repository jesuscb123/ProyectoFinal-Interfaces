package dam2.jetpack.proyectofinal.events.domain.repository

import dam2.jetpack.proyectofinal.events.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getAllEvents(): Flow<List<Event>>
    suspend fun getById(eventId: Long): Event
    suspend fun createEvent (event: Event)
    suspend fun deleteEvent (eventId: Long): Int

    suspend fun acceptEvent(event: Event, userEmail: String)

}