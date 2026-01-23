package dam2.jetpack.proyectofinal.user.data.repository

import dam2.jetpack.proyectofinal.user.data.local.dao.UserDao
import dam2.jetpack.proyectofinal.user.data.mapper.toDomain
import dam2.jetpack.proyectofinal.user.data.mapper.toEntity
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación del repositorio de usuarios ([UserRepository]).
 *
 * Esta clase se encarga de coordinar las operaciones de datos de los usuarios,
 * obteniendo los datos desde el DAO ([UserDao]) y mapeándolos desde entidades
 * ([dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity]) a modelos de dominio ([User])
 * y viceversa.
 *
 * @param userDao El objeto de acceso a datos (DAO) para la entidad de usuario.
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    /**
     * Obtiene un usuario por su UID de Firebase.
     *
     * @param firebaseUid El UID de Firebase del usuario a buscar.
     * @return El objeto [User] correspondiente.
     * @throws IllegalStateException si no se encuentra ningún usuario con ese UID.
     */
    override suspend fun getUserByFirebaseUid(firebaseUid: String): User {
        val userEntity = userDao.getUserByFirebaseUid(firebaseUid)
            ?: error("User no encontrado")

        return userEntity.toDomain()
    }

    /**
     * Guarda (inserta o actualiza) un usuario en la base de datos local.
     *
     * @param user El objeto [User] a guardar.
     */
    override suspend fun saveUser(user: User) {
        userDao.insertOrReplaceUser(user.toEntity())
    }

    /**
     * Obtiene un usuario por su dirección de email.
     *
     * @param email El email del usuario a buscar.
     * @return El objeto [User] correspondiente.
     * @throws IllegalStateException si no se encuentra ningún usuario con ese email.
     */
    override suspend fun getUserByEmail(email: String): User {
        val userEntity = userDao.getUserByEmail(email)
            ?: error("User no encontrado")

        return userEntity.toDomain()
    }

    /**
     * Obtiene una lista de todos los usuarios como un flujo de datos reactivo.
     *
     * @return Un [Flow] que emite la lista completa de usuarios ([User]) cada vez que hay cambios.
     */
    override suspend fun getAllUsers(): Flow<List<User>> {
        val userList = userDao.getAllUsers()
        return userList.map {list ->
            list.map { it.toDomain() }
        }
    }
}
