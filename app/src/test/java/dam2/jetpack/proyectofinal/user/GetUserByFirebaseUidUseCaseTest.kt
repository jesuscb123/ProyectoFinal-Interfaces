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

class GetUserByFirebaseUidUseCaseTest {

    private val repo: UserRepository = mockk()
    private val useCase = GetUserByFirebaseUidUseCase(repo)

    @Test
    fun `cuando el repositorio devuelve usuario, retorna Result success`() = runTest {
        val uid = "uid123"
        val expected = User(firebaseUid = uid, email = "a@a.com", rol = Rol.USER, puntos = 0)

        coEvery { repo.getUserByFirebaseUid(uid) } returns expected

        val result = useCase(uid)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expected)

        coVerify(exactly = 1) { repo.getUserByFirebaseUid(uid) }
    }

    @Test
    fun `cuando el repositorio lanza excepcion, retorna Result failure`() = runTest {
        val uid = "uid123"
        coEvery { repo.getUserByFirebaseUid(uid) } throws IllegalStateException("User no encontrado")

        val result = useCase(uid)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("User no encontrado")

        coVerify(exactly = 1) { repo.getUserByFirebaseUid(uid) }
    }
}
