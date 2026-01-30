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

class GetUserByEmailUseCaseTest {

    private val repo: UserRepository = mockk()
    private val useCase = GetUserByEmailUseCase(repo)

    @Test
    fun `cuando el repositorio devuelve usuario, retorna Result success`() = runTest {
        val email = "test@mail.com"
        val expected = User(firebaseUid = "uid123", email = email, rol = Rol.USER, puntos = 10)

        coEvery { repo.getUserByEmail(email) } returns expected

        val result = useCase(email)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expected)

        coVerify(exactly = 1) { repo.getUserByEmail(email) }
    }

    @Test
    fun `cuando el repositorio lanza excepcion, retorna Result failure`() = runTest {
        val email = "test@mail.com"
        coEvery { repo.getUserByEmail(email) } throws RuntimeException("boom")

        val result = useCase(email)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("boom")

        coVerify(exactly = 1) { repo.getUserByEmail(email) }
    }
}
