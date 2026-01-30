package dam2.jetpack.proyectofinal.user

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.rules.MainDispatcherRule
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.usecase.*
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getByUid: GetUserByFirebaseUidUseCase = mockk()
    private val getByEmail: GetUserByEmailUseCase = mockk()
    private val saveUser: SaveUserUseCase = mockk()
    private val getAll: GetAllUsersUseCase = mockk()

    @Test
    fun `getUserByEmail - success coloca user en uiState`() = runTest {
        val user = User(firebaseUid = "abc", email = "a@a.com", rol = Rol.USER, puntos = 10)
        coEvery { getByEmail("a@a.com") } returns Result.success(user)

        val vm = UserViewModel(getByUid, getByEmail, saveUser, getAll)

        vm.uiState.test {
            awaitItem() // initial

            vm.getUserByEmail("a@a.com")
            val last = awaitItem()
            assertThat(last.user).isEqualTo(user)
            assertThat(last.error).isNull()
        }
    }

}
