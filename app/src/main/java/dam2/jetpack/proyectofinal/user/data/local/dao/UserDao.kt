package dam2.jetpack.proyectofinal.user.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity
import dam2.jetpack.proyectofinal.user.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad [UserEntity].
 *
 * Esta interfaz define los métodos para interactuar con la tabla `users` en la base de datos Room,
 * permitiendo operaciones como inserción, actualización y consulta de datos de usuarios.
 */
@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario ([UserEntity]) en la base de datos o lo reemplaza si ya existe.
     *
     * La estrategia de conflicto [OnConflictStrategy.REPLACE] asegura que si se intenta insertar
     * un usuario con una clave primaria que ya existe, el registro antiguo será reemplazado por el nuevo.
     *
     * @param user El objeto [UserEntity] a insertar o reemplazar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUser(user: UserEntity)

    /**
     * Obtiene un usuario de la base de datos por su UID de Firebase.
     *
     * @param firebaseUid El UID de Firebase del usuario a buscar.
     * @return El [UserEntity] correspondiente si se encuentra, o `null` si no existe.
     */
    @Query("SELECT * FROM users WHERE firebaseUid = :firebaseUid LIMIT 1")
    suspend fun getUserByFirebaseUid(firebaseUid: String): UserEntity?

    /**
     * Obtiene un usuario de la base de datos por su dirección de email.
     *
     * @param email El email del usuario a buscar.
     * @return El [UserEntity] correspondiente si se encuentra, o `null` si no existe.
     */
    @Query ("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Obtiene todos los usuarios de la base de datos como un flujo de datos reactivo ([Flow]).
     *
     * @return Un [Flow] que emite una lista de [UserEntity] cada vez que los datos de la tabla cambian.
     */
    @Query ("SELECT * FROM users")
    fun getAllUsers (): Flow<List<UserEntity>>




}
