package dam2.jetpack.proyectofinal.user

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import dam2.jetpack.proyectofinal.user.domain.usecase.GetAllUsersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAllUsersUseCaseTest {

    private val repo: UserRepository = mockk()
    private val useCase = GetAllUsersUseCase(repo)

    @Test
    fun `emite lista de usuarios desde el repositorio`() = runTest {
        val users = listOf(
            User(firebaseUid = "1", email = "a@a.com", rol = Rol.USER, puntos = 0),
            User(firebaseUid = "2", email = "b@b.com", rol = Rol.ADMIN, puntos = 10)
        )
        coEvery { repo.getAllUsers() } returns flowOf(users)

        useCase().test {
            val item = awaitItem()
            assertThat(item).isEqualTo(users)
            awaitComplete()
        }
    }
}
