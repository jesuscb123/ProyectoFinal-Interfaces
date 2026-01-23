package dam2.jetpack.proyectofinal.events.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam2.jetpack.proyectofinal.events.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para la entidad [EventEntity].
 *
 * Proporciona los métodos que la aplicación utiliza para interactuar con la tabla `events`
 * en la base de datos Room, permitiendo operaciones como insertar, consultar y eliminar eventos.
 */
@Dao
interface EventDao {
    /**
     * Inserta un nuevo evento en la base de datos o lo reemplaza si ya existe uno con la misma clave primaria.
     *
     * @param event La entidad [EventEntity] que se va a insertar o reemplazar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceEvent(event: EventEntity)

    /**
     * Recupera un único evento de la base de datos a partir de su ID.
     *
     * @param eventId El ID del evento a buscar.
     * @return La [EventEntity] correspondiente si se encuentra, o `null` en caso contrario.
     */
    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getEventById(eventId: Long): EventEntity?

    /**
     * Elimina un evento de la base de datos a partir de su ID.
     *
     * @param eventId El ID del evento a eliminar.
     * @return El número de filas eliminadas (debería ser 1 si el evento existía).
     */
    @Query("DELETE FROM events WHERE eventId = :eventId")
    suspend fun deleteEventById(eventId: Long): Int

    /**
     * Obtiene todos los eventos de la base de datos, ordenados por fecha de creación descendente.
     *
     * @return Un [Flow] que emite la lista completa de [EventEntity] cada vez que los datos cambian.
     */
    @Query("SELECT * FROM events ORDER BY fechaCreacion DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    /**
     * Obtiene todos los eventos en los que un usuario específico ha sido aceptado.
     * La búsqueda se realiza en el campo `userAccept`.
     *
     * @param userAccept El identificador del usuario aceptado.
     * @return Un [Flow] que emite la lista de eventos asociados al usuario.
     */
    @Query("SELECT * FROM events WHERE userAccept LIKE :userAccept ORDER BY fechaCreacion DESC")
    fun getEventsUser(userAccept: String): Flow<List<EventEntity>>

    /**
     * Obtiene todos los eventos creados por un usuario específico.
     *
     * @param userId El ID del usuario que creó los eventos.
     * @return Un [Flow] que emite la lista de eventos creados por el usuario.
     */
    @Query("SELECT * FROM events WHERE userId LIKE :userId ORDER BY fechaCreacion DESC")
    fun getEventsUserCreate(userId: String): Flow<List<EventEntity>>
}

