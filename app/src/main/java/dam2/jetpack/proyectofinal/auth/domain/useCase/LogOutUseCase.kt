package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso para cerrar la sesión de un usuario.
 *
 * Esta clase encapsula la lógica para desautenticar a un usuario, delegando la
 * operación directamente en el [AuthRepository]. Al estar anotada con `@Inject`,
 * puede ser fácilmente inyectada en ViewModels u otras clases que requieran
 * esta funcionalidad.
 *
 * @property authRepository El repositorio que gestiona las operaciones de autenticación.
 */
class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Ejecuta el caso de uso de cierre de sesión.
     *
     * La sobrecarga del operador `invoke` permite que la clase se llame como si fuera una función
     * (ej: `logOutUseCase()`).
     *
     * @return Un objeto [Result] que encapsula el resultado de la operación:
     *         - `Result.success(Unit)`: Si el cierre de sesión fue exitoso.
     *         - `Result.failure(Exception)`: Si ocurrió un error durante el proceso.
     */
    suspend operator fun invoke(): Result<Unit>{
        return try{
            val logOutIntent = authRepository.logOut()
            Result.success(logOutIntent)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
