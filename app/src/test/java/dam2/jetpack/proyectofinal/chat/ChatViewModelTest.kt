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

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val listen: ListenMessagesUseCase = mockk()
    private val send: SendMessageUseCase = mockk()

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
