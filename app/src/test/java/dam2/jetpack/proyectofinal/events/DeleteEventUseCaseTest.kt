package dam2.jetpack.proyectofinal.events

import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import dam2.jetpack.proyectofinal.events.domain.usecase.DeleteEventUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteEventUseCaseTest {

    private val repo: EventRepository = mockk()
    private val useCase = DeleteEventUseCase(repo)

    @Test
    fun `si repo devuelve 0 filas - devuelve failure`() = runTest {
        coEvery { repo.deleteEvent(5L) } returns 0

        val result = useCase(5L)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("No se encontró ningún evento")
    }
}
