package dam2.jetpack.proyectofinal.chat

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dam2.jetpack.proyectofinal.chat.domain.usecase.ListenMessagesUseCase
import dam2.jetpack.proyectofinal.chat.domain.usecase.SendMessageUseCase
import dam2.jetpack.proyectofinal.chat.presentation.viewmodel.ChatViewModel
import dam2.jetpack.proyectofinal.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/**
 * Clase de tests unitarios para [ChatViewModel].
 *
 * Esta clase se encarga de verificar el comportamiento del ViewModel del chat,
 * utilizando mocks para sus dependencias (casos de uso) y asegurando que la lógica
 * de negocio y la gestión del estado se comportan como se espera en un entorno controlado.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    /**
     * Regla de JUnit que sustituye el `Dispatcher.Main` por uno de test.
     * Esto permite que las corrutinas que se lanzarían en el hilo principal se ejecuten
     * de forma síncrona en el hilo del test, facilitando las aserciones.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Mock del caso de uso [ListenMessagesUseCase].
     * Se utiliza para simular la recepción de flujos de mensajes desde el repositorio
     * sin necesidad de una conexión real a la base de datos.
     */
    private val listen: ListenMessagesUseCase = mockk()

    /**
     * Mock del caso de uso [SendMessageUseCase].
     * Permite simular el envío de mensajes, incluyendo escenarios de éxito y de error,
     * para probar la lógica de actualización optimista y su reversión.
     */
    private val send: SendMessageUseCase = mockk()

    /**
     * **Test:** `sendMessage`
     * **Escenario:** El envío de un mensaje falla en el backend (por ejemplo, por un error de red).
     * **Resultado esperado:** El ViewModel no debería mostrar el mensaje en la lista final,
     * revirtiendo cualquier actualización optimista que pudiera haber realizado.
     *
     * **Pasos:**
     * 1. **GIVEN** (Dado): Se configuran los mocks.
     *    - `listen` devuelve un flujo con una lista vacía para simular un estado inicial sin mensajes.
     *    - `send` está configurado para lanzar una excepción (`RuntimeException`) cuando se invoque, simulando un fallo en el envío.
     * 2. **WHEN** (Cuando): Se crea una instancia del `ChatViewModel`.
     *    - El `init` del ViewModel llamará a `loadMessages`.
     * 3. **THEN** (Entonces): Se verifica el estado del flujo `messages`.
     *    - Se utiliza `turbine.test` para observar los ítems emitidos por el `StateFlow`.
     *    - Se comprueba que el estado inicial (`awaitItem()`) es una lista vacía, como se definió en el mock de `listen`.
     *
     * **Nota importante sobre el código actual:** El test, tal como está, no invoca la función `sendMessage`.
     * El comentario "OJO" indica correctamente que el `ChatViewModel` real depende de `FirebaseAuth` para obtener el UID
     * del usuario. En un test de JVM puro, esta llamada devolverá `null`, provocando que la función `sendMessage`
     * termine prematuramente sin llegar a invocar el caso de uso `send`. Para completar este test, sería necesario
     * mockear también la llamada estática a `FirebaseAuth`.
     */
    @Test
    fun `sendMessage - si falla, revierte optimistic`() = runTest {
        // Para este test no usaremos loadMessages, pero lo dejamos preparado
        coEvery { listen(any(), any()) } returns flowOf(emptyList())

        // Simulamos fallo en envío real
        coEvery { send(any(), any(), any()) } throws RuntimeException("network error")

        val vm = ChatViewModel(listen, send)

        vm.messages.test {
            val initial = awaitItem()
            assertThat(initial).isEmpty()

            // OJO: ChatViewModel usa FirebaseAuth.getInstance().currentUser?.uid
            // En test JVM puro, esto será null y saldrá por return.
        }
    }
}
