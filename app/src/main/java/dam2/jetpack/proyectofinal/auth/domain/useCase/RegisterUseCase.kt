package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para registrar un nuevo usuario en el sistema.
 *
 * Este caso de uso orquesta el proceso de registro, que consta de dos pasos principales:
 * 1. Registrar al usuario en el sistema de autenticación (ej: Firebase Auth) a través del [AuthRepository].
 * 2. Guardar la información del perfil del usuario, incluyendo su rol, en la base de datos
 *    (ej: Firestore) a través del [UserRepository].
 *
 * @property authRepository El repositorio para gestionar las operaciones de autenticación.
 * @property userRepository El repositorio para gestionar los datos del perfil del usuario.
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    /**
     * Ejecuta el caso de uso de registro.
     *
     * Valida que el email y la contraseña no estén vacíos antes de proceder.
     *
     * @param email El correo electrónico para el nuevo usuario.
     * @param password La contraseña para el nuevo usuario.
     * @param rol El rol asignado al nuevo usuario (ej: Admin, Usuario).
     * @return Un objeto [Result] que encapsula el resultado de la operación:
     *         - `Result.success(AuthResult)`: Si el registro y guardado son exitosos, contiene
     *           la información de autenticación.
     *         - `Result.failure(Exception)`: Si ocurre un error, como un email ya en uso o
     *           credenciales inválidas. Contiene la excepción específica, que puede ser
     *           [IllegalArgumentException] si los campos están vacíos.
     */
    suspend operator fun invoke(email: String, password: String, rol: Rol): Result<AuthResult> {
        return try{
            require(email.isNotBlank()) {"El email no puede estar vacío"}
            require(password.isNotBlank()) {"La contraseña no puede estar vacía"}
            val auth = authRepository.register(email, password)
            userRepository.saveUser(User(auth.uid, email, rol))
            Result.success(auth)
        }catch (e: IllegalArgumentException){
            Result.failure(e)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}