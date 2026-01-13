package dam2.jetpack.proyectofinal.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dam2.jetpack.proyectofinal.user.data.local.dao.UserDao
import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity
import dam2.jetpack.proyectofinal.user.data.local.converter.RolConverter

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(RolConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
