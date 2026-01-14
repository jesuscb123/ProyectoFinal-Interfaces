package dam2.jetpack.proyectofinal.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dam2.jetpack.proyectofinal.user.data.local.dao.UserDao
import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity
import dam2.jetpack.proyectofinal.user.data.local.converter.RolConverter
import dam2.jetpack.proyectofinal.events.data.local.Converter.DateConverter
import dam2.jetpack.proyectofinal.events.data.local.dao.EventDao
import dam2.jetpack.proyectofinal.events.data.local.Converter.CategoryConverter
import dam2.jetpack.proyectofinal.events.data.local.entity.EventEntity

@Database(entities = [UserEntity::class, EventEntity::class], version = 1, exportSchema = false)
@TypeConverters(RolConverter::class, DateConverter::class, CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
}
