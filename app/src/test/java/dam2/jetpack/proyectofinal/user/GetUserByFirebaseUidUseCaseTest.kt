package dam2.jetpack.proyectofinal.user

import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByFirebaseUidUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Clase de tests unitarios para el caso de uso [GetUserByFirebaseUidUseCase].
 *
 * Esta clase se encarga de verificar que la lógica para obtener un usuario a través de su
 * UID de Firebase funciona como se espera. Se aísla el caso de uso de sus dependencias
 * externas (el repositorio) mediante el uso de mocks.
 */
class GetUserByFirebaseUidUseCaseTest {

    /**
     * Mock del [UserRepository]. Este objeto simulado nos permite controlar
     * las respuestas de la capa de datos (p. ej., una base de datos o una API)
     * sin necesidad de una conexión real.
     */
    private val repo: UserRepository = mockk()

    /**
     * Instancia del caso de uso [GetUserByFirebaseUidUseCase] que se va a probar.
     * Se le inyecta el repositorio mockeado para poder controlar su comportamiento.
     */
    private val useCase = GetUserByFirebaseUidUseCase(repo)

    /**
     * **Test:** Probar el escenario de éxito donde el repositorio encuentra y devuelve un usuario.
     * **Escenario:** El [UserRepository] es capaz de encontrar un usuario con el UID proporcionado.
     * **Resultado esperado:** El caso de uso debe devolver un [Result.success] conteniendo el objeto `User` encontrado.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se define un UID de prueba y un objeto `User` esperado.
     *    - Se configura el mock del repositorio (`coEvery`) para que al llamar a `getUserByFirebaseUid`
     *      con ese UID, devuelva el objeto `User` esperado.
     * 2. **WHEN** (Cuando): Se invoca el caso de uso con el UID.
     * 3. **THEN** (Entonces): Se realizan las aserciones sobre el resultado.
     *    - Se verifica que el resultado es de tipo éxito (`isSuccess`).
     *    - Se comprueba que el usuario dentro del resultado (`getOrNull()`) es el esperado.
     *    - Se confirma (`coVerify`) que el método del repositorio fue llamado exactamente una vez con el UID correcto.
     */
    @Test
    fun `cuando el repositorio devuelve usuario, retorna Result success`() = runTest {
        val uid = "uid123"
        val expected = User(firebaseUid = uid, email = "a@a.com", rol = Rol.USER, puntos = 0)

        // GIVEN: El repositorio devolverá un usuario cuando se le pida por UID.
        coEvery { repo.getUserByFirebaseUid(uid) } returns expected

        // WHEN: Se ejecuta el caso de uso.
        val result = useCase(uid)

        // THEN: El resultado es exitoso y contiene al usuario.
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expected)

        // Y se verifica la interacción con el repositorio.
        coVerify(exactly = 1) { repo.getUserByFirebaseUid(uid) }
    }

    /**
     * **Test:** Probar el escenario de fallo, donde el repositorio lanza una excepción.
     * **Escenario:** El [UserRepository] no encuentra al usuario y lanza una excepción para indicarlo.
     * **Resultado esperado:** El caso de uso debe capturar esta excepción y devolverla dentro de un [Result.failure].
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se define un UID de prueba.
     *    - Se configura el mock del repositorio para que lance una `IllegalStateException`
     *      cuando se intente obtener el usuario con ese UID.
     * 2. **WHEN** (Cuando): Se invoca el caso de uso.
     * 3. **THEN** (Entonces): Se realizan las aserciones sobre el resultado.
     *    - Se verifica que el resultado es de tipo fallo (`isFailure`).
     *    - Se comprueba que el mensaje de la excepción contenida en el resultado es el esperado.
     *    - Se confirma que se intentó llamar al método del repositorio.
     */
    @Test
    fun `cuando el repositorio lanza excepcion, retorna Result failure`() = runTest {
        val uid = "uid123"
        // GIVEN: El repositorio fallará con una excepción.
        coEvery { repo.getUserByFirebaseUid(uid) } throws IllegalStateException("User no encontrado")

        // WHEN: Se ejecuta el caso de uso.
        val result = useCase(uid)

        // THEN: El resultado es un fallo y contiene la excepción esperada.
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("User no encontrado")

        // Y se verifica que la interacción con el repositorio se intentó.
        coVerify(exactly = 1) { repo.getUserByFirebaseUid(uid) }
    }
}
