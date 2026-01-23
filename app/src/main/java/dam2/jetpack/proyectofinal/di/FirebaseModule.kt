package dam2.jetpack.proyectofinal.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar las dependencias de Firebase.
 *
 * Este módulo se encarga de crear y proveer las instancias singleton de servicios
 * clave de Firebase como FirebaseAuth y FirebaseFirestore, haciéndolas disponibles
 * para ser inyectadas en otras partes de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Proporciona una instancia única (Singleton) de [FirebaseAuth].
     *
     * @return La instancia singleton de [FirebaseAuth] para gestionar la autenticación de usuarios.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Proporciona una instancia única (Singleton) de [FirebaseFirestore].
     *
     * @return La instancia singleton de [FirebaseFirestore] para interactuar con la base de datos NoSQL.
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
