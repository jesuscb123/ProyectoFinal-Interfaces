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

/**
 * Clase principal de la base de datos de la aplicación, construida con Room.
 *
 * Esta clase abstracta define la configuración de la base de datos, incluyendo las
 * entidades que contiene, la versión del esquema, y los convertidores de tipos.
 * También proporciona acceso a los Data Access Objects (DAOs) para interactuar
 * con las tablas de la base de datos.
 */
@Database(entities = [UserEntity::class, EventEntity::class], version = 1, exportSchema = false)
@TypeConverters(RolConverter::class, DateConverter::class, CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Proporciona una instancia del Data Access Object (DAO) para la entidad [UserEntity].
     *
     * @return El [UserDao] para realizar operaciones CRUD en la tabla de usuarios.
     */
    abstract fun userDao(): UserDao

    /**
     * Proporciona una instancia del Data Access Object (DAO) para la entidad [EventEntity].
     *
     * @return El [EventDao] para realizar operaciones CRUD en la tabla de eventos.
     */
    abstract fun eventDao(): EventDao
}
