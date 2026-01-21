package dam2.jetpack.proyectofinal.user.data.repository

import dam2.jetpack.proyectofinal.user.data.local.dao.UserDao
import dam2.jetpack.proyectofinal.user.data.mapper.toDomain
import dam2.jetpack.proyectofinal.user.data.mapper.toEntity
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    override suspend fun getUserByFirebaseUid(firebaseUid: String): User {
        val userEntity = userDao.getUserByFirebaseUid(firebaseUid)
            ?: error("User no encontrado")

        return userEntity.toDomain()
    }

    override suspend fun saveUser(user: User) {
        userDao.insertOrReplaceUser(user.toEntity())
    }

    override suspend fun getUserByEmail(email: String): User {
        val userEntity = userDao.getUserByEmail(email)
            ?: error("User no encontrado")

        return userEntity.toDomain()
    }

    override suspend fun getAllUsers(): Flow<List<User>> {
        val userList = userDao.getAllUsers()
        return userList.map {list ->
            list.map { it.toDomain() }
        }
    }
}