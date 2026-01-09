package dam2.jetpack.proyectofinal.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
): AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): AuthResult {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

        val user = result.user ?: error("User no encontrado")

        return AuthResult(
            uid = user.uid,
            email = user.email
        )
    }

    override suspend fun register(
        email: String,
        password: String
    ): AuthResult {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

        val user = result.user ?: error("User no ha podido registrarse.")

        return AuthResult(user.uid, user.email)

    }

    override suspend fun logOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserUid(): String {
       return firebaseAuth.currentUser?.uid
            ?: error("No hay usuario logueado")

    }
}