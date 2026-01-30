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

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAllEventsUseCase: GetAllEventsUseCase = mockk()
    private val getEventByIdUseCase: GetEventByIdUseCase = mockk()
    private val createEventUseCase: CreateEventUseCase = mockk()
    private val deleteEventUseCase: DeleteEventUseCase = mockk()
    private val acceptEventUseCase: AcceptEventUseCase = mockk()
    private val getEventsUserUseCase: GetEventsUserUseCase = mockk()
    private val getEventsUserCreateUseCase: GetEventsUserCreateUseCase = mockk()
    private val getUserByEmailUseCase: GetUserByEmailUseCase = mockk()
    private val saveUserUseCase: SaveUserUseCase = mockk()
    private val getEventStatsUseCase: GetEventStatsUseCase = mockk()

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

    @Test
    fun `loadEventStats - cuando success actualiza contadores`() = runTest {
        coEvery { getEventStatsUseCase() } returns Result.success(7 to 2)

        val vm = createVm()

        vm.uiState.test {
            val initial = awaitItem()
            assertThat(initial.completedEventsCount).isEqualTo(0)

            vm.loadEventStats()

            var last = awaitItem()
            while (last.isLoading) {
                last = awaitItem()
            }

            assertThat(last.completedEventsCount).isEqualTo(7)
            assertThat(last.acceptedEventsCount).isEqualTo(2)
            assertThat(last.errorMessage).isNull()
        }
    }

    @Test
    fun `loadEventStats - cuando failure muestra error`() = runTest {
        coEvery { getEventStatsUseCase() } returns Result.failure(RuntimeException("DB KO"))

        val vm = createVm()

        vm.uiState.test {
            awaitItem() // initial
            vm.loadEventStats()

            var last = awaitItem()
            while (last.isLoading) last = awaitItem()

            assertThat(last.errorMessage).contains("DB KO")
        }
    }
}
