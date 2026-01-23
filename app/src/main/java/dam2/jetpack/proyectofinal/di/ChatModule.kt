package dam2.jetpack.proyectofinal.chat.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dam2.jetpack.proyectofinal.chat.data.repository.ChatRepositoryImpl
import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar las dependencias relacionadas con el chat.
 *
 * Este módulo se encarga de definir cómo se deben crear y proveer las instancias
 * de las clases relacionadas con la funcionalidad de chat, como los repositorios.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    /**
     * Proporciona la implementación concreta para la interfaz [ChatRepository].
     *
     * La anotación `@Binds` le dice a Hilt que cuando se solicite una dependencia
     * de tipo [ChatRepository], debe usar una instancia de [ChatRepositoryImpl].
     * La anotación `@Singleton` asegura que solo se cree una única instancia de este
     * repositorio durante el ciclo de vida de la aplicación.
     *
     * @param impl La implementación concreta del repositorio de chat.
     * @return Una instancia de [ChatRepository].
     */
    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository
}
