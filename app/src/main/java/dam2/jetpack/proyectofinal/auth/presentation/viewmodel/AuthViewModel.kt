package dam2.jetpack.proyectofinal.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam2.jetpack.proyectofinal.auth.domain.useCase.LogOutUseCase
import dam2.jetpack.proyectofinal.auth.domain.useCase.LoginUseCase
import dam2.jetpack.proyectofinal.auth.domain.useCase.RegisterUseCase
import dam2.jetpack.proyectofinal.auth.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val logOutUseCase: LogOutUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = loginUseCase(email, password)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, isAuthenticated = true, error = null)
                },
                onFailure = {
                    _uiState.value.copy(isLoading = false, error = it.message ?: "Error al iniciar sesión.")
                }
            )
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = registerUseCase(email, password)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, isAuthenticated = true, error = null)
                },
                onFailure = { e ->
                    _uiState.value.copy(isLoading = false, error = e.message ?: "Error al registrase.")
                }
            )
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = logOutUseCase()

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, isAuthenticated = false, error = null)
                },
                onFailure = {
                    _uiState.value.copy(isLoading = false,  error = it.message ?: "Error al cerrar sesión.")
                }
            )
        }
    }
}