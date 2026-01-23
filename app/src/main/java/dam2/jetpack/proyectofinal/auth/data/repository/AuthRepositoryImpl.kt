package dam2.jetpack.proyectofinal.auth.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementación del repositorio de autenticación [AuthRepository] que utiliza Firebase Authentication
 * como fuente de datos.
 *
 * Esta clase se encarga de toda la comunicación con Firebase para las operaciones de
 * inicio de sesión, registro y cierre de sesión. Al estar anotada con `@Inject`, Hilt
 * puede proveerla a otras clases (como los casos de uso).
 *
 * @property firebaseAuth Instancia de [FirebaseAuth] inyectada por Hilt, que permite interactuar
 * con el servicio de autenticación de Firebase.
 */
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
): AuthRepository {

    /**
     * Realiza el inicio de sesión de un usuario con su correo y contraseña en Firebase.
     *
     * Envuelve la llamada a la API de Firebase `signInWithEmailAndPassword` en un bloque try-catch
     * para gestionar los posibles errores de autenticación y traducirlos a mensajes
     * comprensibles para el usuario.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Un objeto [AuthResult] con el UID y el email del usuario si el inicio de sesión es exitoso.
     * @throws IllegalArgumentException si los datos son incorrectos, la conexión falla o el usuario no existe.
     * El mensaje de la excepción está personalizado para ser mostrado en la UI.
     */
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

    /**
     * Registra un nuevo usuario en Firebase con su correo y contraseña.
     *
     * @param email El correo electrónico para la nueva cuenta.
     * @param password La contraseña para la nueva cuenta.
     * @return Un objeto [AuthResult] con el UID y el email del usuario recién creado.
     * @throws Exception si el proceso de registro falla por algún motivo (ej. el correo ya existe).
     * @throws IllegalStateException si Firebase no devuelve un objeto de usuario tras el registro.
     */
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