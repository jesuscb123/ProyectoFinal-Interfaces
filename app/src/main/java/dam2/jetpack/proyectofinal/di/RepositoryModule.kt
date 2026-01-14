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

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository

}