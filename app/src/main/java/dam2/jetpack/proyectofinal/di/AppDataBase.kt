package dam2.jetpack.proyectofinal.di

import androidx.room.Database
import androidx.room.RoomDatabase
import dam2.jetpack.proyectofinal.user.data.local.dao.UserDao
import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
