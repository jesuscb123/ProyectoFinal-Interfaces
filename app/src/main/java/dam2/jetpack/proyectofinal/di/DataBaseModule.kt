package dam2.jetpack.proyectofinal.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dam2.jetpack.proyectofinal.events.data.local.dao.EventDao
import dam2.jetpack.proyectofinal.user.data.local.dao.UserDao
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar las dependencias relacionadas con la base de datos Room.
 *
 * Este módulo se encarga de construir y proveer la instancia de la base de datos [AppDatabase]
 * y los Data Access Objects (DAOs) que dependen de ella.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Proporciona una instancia única (Singleton) de la base de datos [AppDatabase].
     *
     * Utiliza el constructor de Room para crear la base de datos. `fallbackToDestructiveMigration`
     * indica a Room que recree las tablas desde cero si no se proporciona una estrategia de migración,
     * lo cual es útil durante el desarrollo pero debe manejarse con cuidado en producción.
     *
     * @param context El contexto de la aplicación, inyectado por Hilt.
     * @return La instancia singleton de [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()

    /**
     * Proporciona una instancia del [UserDao].
     *
     * @param db La instancia de [AppDatabase] de la que se obtendrá el DAO.
     * @return El DAO para la entidad de usuario.
     */
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    /**
     * Proporciona una instancia del [EventDao].
     *
     * @param db La instancia de [AppDatabase] de la que se obtendrá el DAO.
     * @return El DAO para la entidad de evento.
     */
    @Provides
    fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()
}
