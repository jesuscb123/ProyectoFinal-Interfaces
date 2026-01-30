package dam2.jetpack.proyectofinal.events

import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import dam2.jetpack.proyectofinal.events.domain.usecase.GetEventStatsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetEventStatsUseCaseTest {

    private val repo: EventRepository = mockk()
    private val useCase = GetEventStatsUseCase(repo)

    @Test
    fun `cuando repo devuelve stats - useCase devuelve success con Pair`() = runTest {
        coEvery { repo.getEventStats() } returns Result.success(10 to 3)

        val result = useCase()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(10 to 3)
        coVerify(exactly = 1) { repo.getEventStats() }
    }
}
