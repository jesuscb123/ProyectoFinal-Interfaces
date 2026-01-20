package dam2.jetpack.proyectofinal.auth.data.repository

import android.util.Log
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
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalArgumentException("usuario no encontrado")

            return AuthResult(
                uid = user.uid,
                email = user.email
            )

        } catch (e: Exception) {
            val mensaje = when {
                e.message?.contains("password") == true -> "La contraseña es incorrecta"
                e.message?.contains("no user record") == true -> "No existe una cuenta con ese correo"
                e.message?.contains("badly formatted") == true -> "El correo no es válido"
                e.message?.contains("network") == true -> "Error de conexión"
                else -> "Error al iniciar sesión"
            }

            throw IllegalArgumentException(mensaje)
        }
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