package dam2.jetpack.proyectofinal.events.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam2.jetpack.proyectofinal.events.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceEvent(event: EventEntity)

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getEventById(eventId: Long): EventEntity?

    @Query("DELETE FROM events WHERE eventId = :eventId")
    suspend fun deleteEventById(eventId: Long): Int

    @Query("SELECT * FROM events ORDER BY fechaCreacion DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE userAccept LIKE :userAccept ORDER BY fechaCreacion DESC")
    fun getEventsUser(userAccept: String): Flow<List<EventEntity>>





}