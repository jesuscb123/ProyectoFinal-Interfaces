package dam2.jetpack.proyectofinal.events

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.events.domain.usecase.*
import dam2.jetpack.proyectofinal.rules.MainDispatcherRule
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByEmailUseCase
import dam2.jetpack.proyectofinal.user.domain.usecase.SaveUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/**
 * Clase de tests unitarios para [EventViewModel].
 *
 * Esta clase se enfoca en verificar la lógica de negocio y la gestión del estado (UIState)
 * del ViewModel principal de eventos. Utiliza mocks para todos los casos de uso (dependencias)
 * para asegurar que el ViewModel reacciona correctamente a los resultados (éxito o fallo)
 * que estos le proporcionan.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModelTest {

    /**
     * Regla de JUnit que asegura que las corrutinas que usan el Dispatcher.Main
     * se ejecuten de forma síncrona en el hilo de test. Es fundamental para
     * probar ViewModels que lanzan corrutinas en `viewModelScope`.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Mocks para todos los casos de uso de los que depende el EventViewModel.
     * Al usar `mockk()`, creamos objetos simulados que nos permiten definir su
     * comportamiento en cada test (p. ej., qué deben devolver cuando se les llama).
     */
    private val getAllEventsUseCase: GetAllEventsUseCase = mockk()
    private val getEventByIdUseCase: GetEventByIdUseCase = mockk()
    private val createEventUseCase: CreateEventUseCase = mockk()
    private val deleteEventUseCase: DeleteEventUseCase = mockk()
    private val acceptEventUseCase: AcceptEventUseCase = mockk()
    private val getEventsUserUseCase: GetEventsUserUseCase = mockk()
    private val getEventsUserCreateUseCase: GetEventsUserCreateUseCase = mockk()
    private val getUserByEmailUseCase: GetUserByEmailUseCase = mockk()
    private val saveUserUseCase: SaveUserUseCase = mockk()
    private val getEventStatsUseCase: GetEventStatsUseCase = mockk(relaxed = true) // Relaxed para no requerir coEvery si no se usa

    /**
     * Función de ayuda (helper) para crear una instancia fresca del [EventViewModel]
     * para cada test. Inyecta todos los mocks definidos anteriormente.
     * Esto asegura que los tests son independientes y no comparten estado.
     *
     * @return Una nueva instancia de [EventViewModel].
     */
    private fun createVm() = dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel(
        getAllEventsUseCase,
        getEventByIdUseCase,
        createEventUseCase,
        deleteEventUseCase,
        acceptEventUseCase,
        getEventsUserUseCase,
        getEventsUserCreateUseCase,
        getUserByEmailUseCase,
        saveUserUseCase,
        getEventStatsUseCase
    )

    /**
     * **Test:** `loadEventStats`
     * **Escenario:** El caso de uso [GetEventStatsUseCase] se ejecuta con éxito y devuelve los contadores.
     * **Resultado esperado:** El `uiState` del ViewModel debe actualizarse reflejando los nuevos
     *              contadores de eventos completados y aceptados, y no debe mostrar ningún mensaje de error.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se configura el mock `getEventStatsUseCase` para que devuelva un `Result.success`
     *    con un par de valores (7 completados, 2 aceptados).
     * 2. **WHEN** (Cuando): Se crea el ViewModel y se llama a la función `loadEventStats()`.
     * 3. **THEN** (Entonces): Se utiliza `turbine.test` para observar el flujo de `uiState`.
     *    - Se consume el estado inicial.
     *    - Se consumen los estados intermedios de carga (`isLoading = true`).
     *    - Se verifica que el estado final contiene los valores correctos (`completedEventsCount = 7`, `acceptedEventsCount = 2`)
     *      y que el mensaje de error es nulo.
     */
    @Test
    fun `loadEventStats - cuando success actualiza contadores`() = runTest {
        // GIVEN
        coEvery { getEventStatsUseCase() } returns Result.success(7 to 2)

        // WHEN
        val vm = createVm()

        vm.uiState.test {
            val initial = awaitItem()
            assertThat(initial.completedEventsCount).isEqualTo(0) // Estado inicial

            vm.loadEventStats()

            // THEN
            var last = awaitItem()
            while (last.isLoading) { // Consumir estados de carga
                last = awaitItem()
            }

            assertThat(last.completedEventsCount).isEqualTo(7)
            assertThat(last.acceptedEventsCount).isEqualTo(2)
            assertThat(last.errorMessage).isNull()
        }
    }

    /**
     * **Test:** `loadEventStats`
     * **Escenario:** El caso de uso [GetEventStatsUseCase] falla (p. ej., por un error en la base de datos).
     * **Resultado esperado:** El `uiState` del ViewModel debe actualizarse para mostrar un mensaje
     *              de error y los contadores no deben cambiar.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se configura el mock `getEventStatsUseCase` para que devuelva un `Result.failure`
     *    con una `RuntimeException`.
     * 2. **WHEN** (Cuando): Se crea el ViewModel y se llama a la función `loadEventStats()`.
     * 3. **THEN** (Entonces): Se observa el flujo `uiState`.
     *    - Tras los estados de carga, se verifica que el estado final contiene un mensaje de error
     *      que incluye el texto de la excepción simulada ("DB KO").
     */
    @Test
    fun `loadEventStats - cuando failure muestra error`() = runTest {
        // GIVEN
        coEvery { getEventStatsUseCase() } returns Result.failure(RuntimeException("DB KO"))

        // WHEN
        val vm = createVm()

        vm.uiState.test {
            awaitItem() // Estado inicial
            vm.loadEventStats()

            // THEN
            var last = awaitItem()
            while (last.isLoading) last = awaitItem() // Consumir estados de carga

            assertThat(last.errorMessage).contains("DB KO")
        }
    }
}
