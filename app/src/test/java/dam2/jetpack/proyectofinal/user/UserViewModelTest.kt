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

/**
 * Clase de tests unitarios para el [UserViewModel].
 *
 * El objetivo es verificar que el ViewModel gestiona correctamente su estado (UIState)
 * en respuesta a las llamadas de sus funciones y a los resultados (éxito o fallo)
 * que le proporcionan sus dependencias (los casos de uso).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    /**
     * Regla de JUnit que sustituye el `Dispatcher.Main` por un `TestDispatcher`.
     * Esto es crucial para que las corrutinas del `viewModelScope` se ejecuten de
     * forma controlada y síncrona en el hilo del test, permitiendo hacer
     * aserciones predecibles sobre los cambios de estado.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks de todos los casos de uso de los que depende el UserViewModel.
    // Al usar `mockk()`, podemos simular su comportamiento para cada test.
    private val getByUid: GetUserByFirebaseUidUseCase = mockk()
    private val getByEmail: GetUserByEmailUseCase = mockk()
    private val saveUser: SaveUserUseCase = mockk()
    private val getAll: GetAllUsersUseCase = mockk()

    /**
     * **Test:** `getUserByEmail`
     * **Escenario:** El caso de uso [GetUserByEmailUseCase] devuelve un resultado exitoso (`Result.success`).
     * **Resultado esperado:** El `uiState` del ViewModel debe actualizarse para contener
     *              el objeto `User` recibido y no debe mostrar ningún error.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se crea un objeto `User` de ejemplo.
     *    - Se configura el mock `getByEmail` para que, cuando se le llame con "a@a.com",
     *      devuelva un `Result.success` conteniendo el usuario de ejemplo.
     * 2. **WHEN** (Cuando): Se crea una instancia del `UserViewModel` y se invoca
     *    la función `getUserByEmail` que se quiere probar.
     * 3. **THEN** (Entonces): Se utiliza `turbine.test` para observar el `StateFlow` del `uiState`.
     *    - Se consume el estado inicial (`awaitItem()`).
     *    - Después de llamar a la función del ViewModel, se captura el último estado emitido.
     *    - Se verifica (`assertThat`) que la propiedad `user` de este estado es igual al usuario de ejemplo.
     *    - Se verifica que la propiedad `error` es nula, confirmando que no se ha producido ningún fallo.
     */
    @Test
    fun `getUserByEmail - success coloca user en uiState`() = runTest {
        // GIVEN: Un usuario y un caso de uso que lo devuelve con éxito.
        val user = User(firebaseUid = "abc", email = "a@a.com", rol = Rol.USER, puntos = 10)
        coEvery { getByEmail("a@a.com") } returns Result.success(user)

        // WHEN: Se crea el ViewModel.
        val vm = UserViewModel(getByUid, getByEmail, saveUser, getAll)

        vm.uiState.test {
            awaitItem() // Consumir el estado inicial.

            // Se invoca la función a probar.
            vm.getUserByEmail("a@a.com")

            // THEN: El estado de la UI se actualiza correctamente.
            val last = awaitItem()
            assertThat(last.user).isEqualTo(user)
            assertThat(last.error).isNull()
        }
    }

}
