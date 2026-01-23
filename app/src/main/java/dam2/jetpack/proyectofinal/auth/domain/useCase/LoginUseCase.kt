package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject
/**
 * Caso de uso para gestionar el flujo completo de inicio de sesión de un usuario.
 *
 * Este caso de uso orquesta la lógica de negocio para un login, que implica dos pasos:
 * 1. Autenticar al usuario con sus credenciales (email y contraseña) a través del [AuthRepository].
 * 2. Una vez autenticado, obtener el perfil completo del usuario (incluyendo sus roles, como 'isAdmin')
 *    desde el [UserRepository] usando el UID devuelto por la autenticación.
 *
 * Al estar anotado con `@Inject`, Hilt puede inyectar las dependencias necesarias (repositorios)
 * y también puede ser inyectado en otras clases, como un ViewModel.
 *
 * @property authRepository El repositorio que gestiona las operaciones de autenticación (ej: Firebase Auth).
 * @property userRepository El repositorio que gestiona los datos de los perfiles de usuario (ej: Firestore).
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Ejecuta el caso de uso de inicio de sesión.
     *
     * La sobrecarga del operador `invoke` permite que la clase se llame como si fuera una función
     * (ej: `loginUseCase(email, password)`).
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Un objeto [Result] que encapsula el resultado de la operación:
     *         - `Result.success(User)`: Si la autenticación y la obtención de datos son exitosas,
     *           contiene el objeto [User] completo.
     *         - `Result.failure(Exception)`: Si ocurre un error en cualquier punto del proceso
     *           (ej: credenciales incorrectas, usuario no encontrado en la base de datos), contiene
     *           la excepción correspondiente.
     */
    suspend operator fun invoke (email: String, password: String): Result<User?>{
        return try{
            val auth = authRepository.login(email, password)
            val user = userRepository.getUserByFirebaseUid(auth.uid)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}

