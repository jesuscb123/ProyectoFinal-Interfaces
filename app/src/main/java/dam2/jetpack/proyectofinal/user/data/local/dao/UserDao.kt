package dam2.jetpack.proyectofinal.user.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE firebaseUid = :firebaseUid LIMIT 1")
    suspend fun getUserByFirebaseUid(firebaseUid: String): UserEntity?



}