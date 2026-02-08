package dam2.jetpack.proyectofinal.events

import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import dam2.jetpack.proyectofinal.events.domain.usecase.GetEventStatsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Clase de tests unitarios para el caso de uso [GetEventStatsUseCase].
 *
 * Esta clase se encarga de verificar que la lógica para obtener las estadísticas
 * de los eventos (eventos completados y aceptados) funciona como se espera,
 * aislando el caso de uso de su dependencia, el [EventRepository].
 */
class GetEventStatsUseCaseTest {

    /**
     * Mock del [EventRepository]. Este objeto simulado nos permite controlar
     * lo que devuelve la capa de datos sin necesidad de una base de datos real.
     * En este test, lo usaremos para simular una respuesta exitosa.
     */
    private val repo: EventRepository = mockk()

    /**
     * La instancia del caso de uso [GetEventStatsUseCase] que estamos probando.
     * Se le inyecta el repositorio mockeado (`repo`).
     */
    private val useCase = GetEventStatsUseCase(repo)

    /**
     * **Test:** Probar el flujo de ejecución cuando el repositorio devuelve las estadísticas con éxito.
     * **Escenario:** El [EventRepository] obtiene los datos correctamente y devuelve un `Result.success`
     *             conteniendo un par de números (por ejemplo, 10 completados y 3 aceptados).
     * **Resultado esperado:** El [GetEventStatsUseCase] debe propagar este resultado exitoso sin modificarlo.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se configura el comportamiento del mock.
     *    - `coEvery` define que cuando se llame a la función `repo.getEventStats()`, esta debe
     *      devolver un `Result.success` con el par de valores `10 to 3`.
     * 2. **WHEN** (Cuando): Se invoca el caso de uso (`useCase`).
     * 3. **THEN** (Entonces): Se realizan las aserciones para verificar el resultado.
     *    - `assertThat(result.isSuccess).isTrue()`: Se comprueba que el resultado final es de tipo éxito.
     *    - `assertThat(result.getOrNull()).isEqualTo(10 to 3)`: Se verifica que el contenido del resultado
     *      es el mismo par de valores que devolvió el repositorio.
     *    - `coVerify(exactly = 1) { repo.getEventStats() }`: Se asegura que la función `getEventStats` del
     *      repositorio fue llamada exactamente una vez, confirmando que la interacción ocurrió como se esperaba.
     */
    @Test
    fun `cuando repo devuelve stats - useCase devuelve success con Pair`() = runTest {
        // GIVEN: El repositorio devuelve un resultado exitoso con un par de estadísticas.
        coEvery { repo.getEventStats() } returns Result.success(10 to 3)

        // WHEN: Se ejecuta el caso de uso.
        val result = useCase()

        // THEN: El resultado del caso de uso es exitoso y contiene los mismos datos.
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(10 to 3)
        // Y se verifica que el método del repositorio fue llamado una sola vez.
        coVerify(exactly = 1) { repo.getEventStats() }
    }
}