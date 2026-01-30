package dam2.jetpack.proyectofinal.user

import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import dam2.jetpack.proyectofinal.user.domain.usecase.SaveUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveUserUseCaseTest {

    private val repo: UserRepository = mockk()
    private val useCase = SaveUserUseCase(repo)

    @Test
    fun `guarda usuario llamando al repositorio`() = runTest {
        val user = User(firebaseUid = "uid", email = "x@x.com", rol = Rol.USER, puntos = 5)
        coEvery { repo.saveUser(user) } returns Unit

        useCase(user)

        coVerify(exactly = 1) { repo.saveUser(user) }
    }
}
