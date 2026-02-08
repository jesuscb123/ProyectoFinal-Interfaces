package dam2.jetpack.proyectofinal.user

import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByEmailUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Clase de tests unitarios para el caso de uso [GetUserByEmailUseCase].
 *
 * Esta clase verifica que el caso de uso para obtener un usuario por su email
 * se comporta correctamente en diferentes situaciones (éxito y fallo),
 * aislando la lógica del caso de uso de su dependencia, el [UserRepository].
 */
class GetUserByEmailUseCaseTest {

    /**
     * Mock del [UserRepository]. Este objeto simulado nos permite definir
     * el comportamiento de la capa de datos (lo que devuelve el repositorio)
     * sin necesidad de interactuar con una base de datos real.
     */
    private val repo: UserRepository = mockk()

    /**
     * La instancia del caso de uso [GetUserByEmailUseCase] que se está probando.
     * Se le inyecta el repositorio mockeado (`repo`) para poder controlar
     * sus respuestas durante los tests.
     */
    private val useCase = GetUserByEmailUseCase(repo)

    /**
     * **Test:** Probar el escenario de éxito, donde el repositorio encuentra y devuelve un usuario.
     * **Escenario:** El [UserRepository] encuentra un usuario con el email proporcionado y lo devuelve.
     * **Resultado esperado:** El caso de uso debe envolver el objeto `User` en un [Result.success].
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se define un email y un objeto `User` de ejemplo.
     *    - Se configura el mock del repositorio (`repo`) para que, cuando se llame a `getUserByEmail`
     *      con ese email, devuelva el objeto `User` esperado.
     * 2. **WHEN** (Cuando): Se invoca el caso de uso (`useCase`) con el email.
     * 3. **THEN** (Entonces): Se realizan las aserciones sobre el resultado.
     *    - `assertThat(result.isSuccess).isTrue()`: Se verifica que el resultado es exitoso.
     *    - `assertThat(result.getOrNull()).isEqualTo(expected)`: Se comprueba que el usuario contenido
     *      en el resultado es el mismo que se esperaba.
     *    - `coVerify(exactly = 1) { ... }`: Se asegura que el método `getUserByEmail` del repositorio
     *      fue llamado exactamente una vez con el email correcto.
     */
    @Test
    fun `cuando el repositorio devuelve usuario, retorna Result success`() = runTest {
        val email = "test@mail.com"
        val expected = User(firebaseUid = "uid123", email = email, rol = Rol.USER, puntos = 10)

        // GIVEN: El repositorio encontrará este usuario.
        coEvery { repo.getUserByEmail(email) } returns expected

        // WHEN: Se ejecuta el caso de uso.
        val result = useCase(email)

        // THEN: El resultado es exitoso y contiene el usuario esperado.
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expected)

        // Y se verifica que la interacción con el repositorio ocurrió.
        coVerify(exactly = 1) { repo.getUserByEmail(email) }
    }

    /**
     * **Test:** Probar el escenario de fallo, donde el repositorio lanza una excepción.
     * **Escenario:** El [UserRepository] falla al buscar el usuario (p. ej., por un error de red o
     *              porque el usuario no existe y la lógica del repo lanza una excepción).
     * **Resultado esperado:** El caso de uso debe capturar la excepción y devolverla dentro de un [Result.failure].
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se define un email.
     *    - Se configura el mock del repositorio para que, cuando se llame a `getUserByEmail`,
     *      lance una `RuntimeException` con un mensaje específico.
     * 2. **WHEN** (Cuando): Se invoca el caso de uso con el email.
     * 3. **THEN** (Entonces): Se realizan las aserciones sobre el resultado.
     *    - `assertThat(result.isFailure).isTrue()`: Se verifica que el resultado es un fallo.
     *    - `assertThat(result.exceptionOrNull()?.message).isEqualTo("boom")`: Se comprueba que
     *      la excepción contenida en el resultado es la misma que se lanzó.
     *    - `coVerify(exactly = 1) { ... }`: Se confirma que se intentó llamar al método del repositorio.
     */
    @Test
    fun `cuando el repositorio lanza excepcion, retorna Result failure`() = runTest {
        val email = "test@mail.com"

        // GIVEN: El repositorio fallará con una excepción.
        coEvery { repo.getUserByEmail(email) } throws RuntimeException("boom")

        // WHEN: Se ejecuta el caso de uso.
        val result = useCase(email)

        // THEN: El resultado es un fallo y contiene la excepción esperada.
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("boom")

        // Y se verifica que la interacción con el repositorio se intentó.
        coVerify(exactly = 1) { repo.getUserByEmail(email) }
    }
}
