package dam2.jetpack.proyectofinal.events

import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import dam2.jetpack.proyectofinal.events.domain.usecase.DeleteEventUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Clase de tests unitarios para el caso de uso [DeleteEventUseCase].
 *
 * El objetivo de esta clase es verificar que la lógica de negocio para eliminar un evento
 * funciona correctamente en diferentes escenarios, aislando el caso de uso de sus
 * dependencias externas, como el repositorio.
 */
class DeleteEventUseCaseTest {

    /**
     * Mock del [EventRepository]. Se utiliza para simular el comportamiento de la capa de datos
     * (por ejemplo, la base de datos) sin necesidad de interactuar con ella realmente.
     * Esto permite controlar las respuestas del repositorio durante los tests.
     */
    private val repo: EventRepository = mockk()

    /**
     * Instancia del caso de uso [DeleteEventUseCase] que se va a probar.
     * Se le inyecta el mock del repositorio para poder controlar su comportamiento.
     */
    private val useCase = DeleteEventUseCase(repo)

    /**
     * **Test:** Invocar el caso de uso para eliminar un evento que no existe.
     * **Escenario:** El repositorio (`repo`) informa que no se ha eliminado ninguna fila (devuelve 0).
     *             Esto sucede típicamente cuando el ID del evento a eliminar no se encuentra en la base de datos.
     * **Resultado esperado:** El caso de uso debe devolver un resultado de tipo [Result.failure].
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se configura el mock del repositorio.
     *    - Usando `coEvery`, se especifica que cuando se llame a `repo.deleteEvent` con el ID `5L`,
     *      debe devolver `0` (simulando 0 filas afectadas).
     * 2. **WHEN** (Cuando): Se invoca el caso de uso (`useCase`) con el mismo ID (`5L`).
     * 3. **THEN** (Entonces): Se realizan las aserciones sobre el resultado.
     *    - Se comprueba que el resultado es un fallo (`result.isFailure` debe ser `true`).
     *    - Se verifica que la excepción contenida en el resultado (`result.exceptionOrNull()`)
     *      incluye un mensaje explicativo, indicando que el evento no fue encontrado.
     */
    @Test
    fun `si repo devuelve 0 filas - devuelve failure`() = runTest {
        // GIVEN: El repositorio no encuentra el evento a borrar y devuelve 0 filas afectadas.
        coEvery { repo.deleteEvent(5L) } returns 0

        // WHEN: Se ejecuta el caso de uso para borrar dicho evento.
        val result = useCase(5L)

        // THEN: El resultado debe ser un fallo con un mensaje de error específico.
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("No se encontró ningún evento")
    }
}
