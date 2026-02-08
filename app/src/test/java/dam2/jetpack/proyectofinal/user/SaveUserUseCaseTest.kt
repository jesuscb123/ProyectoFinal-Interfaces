package dam2.jetpack.proyectofinal.user

import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import dam2.jetpack.proyectofinal.user.domain.usecase.SaveUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Clase de tests unitarios para el caso de uso [SaveUserUseCase].
 *
 * El propósito de esta clase es verificar que el caso de uso para guardar
 * un usuario invoca correctamente al repositorio, delegando la operación
 * de guardado.
 */
class SaveUserUseCaseTest {

    /**
     * Mock del [UserRepository]. Este objeto simulado nos permite verificar
     * las interacciones con la capa de datos sin necesidad de una base de
     * datos o una API real.
     */
    private val repo: UserRepository = mockk()

    /**
     * Instancia del caso de uso [SaveUserUseCase] que se va a probar.
     * Se le inyecta el repositorio mockeado para aislarlo de sus dependencias.
     */
    private val useCase = SaveUserUseCase(repo)

    /**
     * **Test:** Probar que el caso de uso llama al método de guardar del repositorio.
     * **Escenario:** Se invoca el caso de uso con un objeto `User`.
     * **Resultado esperado:** El método `saveUser` del [UserRepository] debe ser
     *              invocado exactamente una vez con el mismo objeto `User`.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se crea un objeto `User` de ejemplo.
     *    - Se configura el mock (`coEvery`) para que, cuando se llame a `repo.saveUser`,
     *      no haga nada y devuelva `Unit` (comportamiento típico de una función `suspend` sin retorno).
     * 2. **WHEN** (Cuando): Se invoca el caso de uso (`useCase`) con el objeto `User`.
     * 3. **THEN** (Entonces): Se verifica la interacción con el mock.
     *    - `coVerify(exactly = 1) { ... }`: Se comprueba que el método `repo.saveUser(user)`
     *      fue llamado exactamente una vez, asegurando que el caso de uso está
     *      delegando la operación correctamente.
     */
    @Test
    fun `guarda usuario llamando al repositorio`() = runTest {
        // GIVEN: Un usuario para guardar y un repositorio preparado para la llamada.
        val user = User(firebaseUid = "uid", email = "x@x.com", rol = Rol.USER, puntos = 5)
        coEvery { repo.saveUser(user) } returns Unit

        // WHEN: Se ejecuta el caso de uso con el usuario.
        useCase(user)

        // THEN: Se verifica que el repositorio fue llamado para guardar ese usuario.
        coVerify(exactly = 1) { repo.saveUser(user) }
    }
}

