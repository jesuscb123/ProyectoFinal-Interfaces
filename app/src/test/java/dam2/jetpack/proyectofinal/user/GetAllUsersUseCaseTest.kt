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

/**
 * Clase de tests unitarios para el caso de uso [GetAllUsersUseCase].
 *
 * El objetivo de esta clase es verificar que el caso de uso obtiene y emite
 * correctamente la lista de todos los usuarios, delegando la llamada a su
 * dependencia, el [UserRepository].
 */
class GetAllUsersUseCaseTest {

    /**
     * Mock del [UserRepository]. Este objeto simulado nos permite controlar
     * la fuente de datos (qué devuelve el repositorio) sin necesidad de
     * conectar con una base de datos real como Firestore.
     */
    private val repo: UserRepository = mockk()

    /**
     * Instancia del caso de uso [GetAllUsersUseCase] que se va a probar.
     * Se le inyecta el repositorio mockeado para aislarlo de sus dependencias.
     */
    private val useCase = GetAllUsersUseCase(repo)

    /**
     * **Test:** Probar que el caso de uso emite la lista de usuarios que recibe del repositorio.
     * **Escenario:** El [UserRepository] emite un flujo (`Flow`) que contiene una lista de usuarios.
     * **Resultado esperado:** El [GetAllUsersUseCase] debe devolver exactamente el mismo flujo con la misma lista de usuarios.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se prepara una lista de usuarios de ejemplo.
     *    - Se configura el mock del repositorio (`repo`) para que, cuando se llame a su función `getAllUsers()`,
     *      devuelva un flujo (`flowOf`) que contiene la lista de usuarios preparada.
     * 2. **WHEN** (Cuando): Se invoca el caso de uso (`useCase`).
     * 3. **THEN** (Entonces): Se utiliza la librería `turbine` para testear el flujo resultante.
     *    - `useCase().test { ... }`: Se suscribe al flujo devuelto por el caso de uso.
     *    - `val item = awaitItem()`: Espera a que el flujo emita el primer (y único) elemento, que es la lista de usuarios.
     *    - `assertThat(item).isEqualTo(users)`: Se comprueba que la lista emitida es idéntica a la lista original.
     *    - `awaitComplete()`: Se verifica que el flujo se completa correctamente después de emitir su valor.
     */
    @Test
    fun `emite lista de usuarios desde el repositorio`() = runTest {
        // GIVEN: Una lista de usuarios de ejemplo y un repositorio que la emite.
        val users = listOf(
            User(firebaseUid = "1", email = "a@a.com", rol = Rol.USER, puntos = 0),
            User(firebaseUid = "2", email = "b@b.com", rol = Rol.ADMIN, puntos = 10)
        )
        coEvery { repo.getAllUsers() } returns flowOf(users)

        // WHEN: Se invoca el caso de uso.
        // THEN: El flujo resultante emite la lista esperada y se completa.
        useCase().test {
            val item = awaitItem()
            assertThat(item).isEqualTo(users)
            awaitComplete()
        }
    }
}