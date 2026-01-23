package dam2.jetpack.proyectofinal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dam2.jetpack.proyectofinal.auth.data.repository.AuthRepositoryImpl
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import dam2.jetpack.proyectofinal.events.data.repository.EventRepositoryImpl
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import dam2.jetpack.proyectofinal.user.data.repository.UserRepositoryImpl
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar las implementaciones de los repositorios de la aplicación.
 *
 * Esta clase abstracta utiliza la anotación `@Binds` de Hilt para indicar qué implementación
 * concreta debe usarse cuando se solicita una interfaz de repositorio. Esto desacopla
 * las interfaces de dominio de sus implementaciones en la capa de datos.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Proporciona la implementación de [AuthRepository].
     *
     * Cuando se inyecta [AuthRepository], Hilt proveerá una instancia de [AuthRepositoryImpl].
     * Se anota con `@Singleton` para asegurar una única instancia a lo largo de la app.
     *
     * @param impl La implementación concreta del repositorio de autenticación.
     * @return Una instancia que implementa [AuthRepository].
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    /**
     * Proporciona la implementación de [UserRepository].
     *
     * Cuando se inyecta [UserRepository], Hilt proveerá una instancia de [UserRepositoryImpl].
     * Se anota con `@Singleton` para asegurar una única instancia a lo largo de la app.
     *
     * @param impl La implementación concreta del repositorio de usuarios.
     * @return Una instancia que implementa [UserRepository].
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    /**
     * Proporciona la implementación de [EventRepository].
     *
     * Cuando se inyecta [EventRepository], Hilt proveerá una instancia de [EventRepositoryImpl].
     * Se anota con `@Singleton` para asegurar una única instancia a lo largo de la app.
     *
     * @param impl La implementación concreta del repositorio de eventos.
     * @return Una instancia que implementa [EventRepository].
     */
    @Binds
    @Singleton
    abstract fun bindEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository

}
