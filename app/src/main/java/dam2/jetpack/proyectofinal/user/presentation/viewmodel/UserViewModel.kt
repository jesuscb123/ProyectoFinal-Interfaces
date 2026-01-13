package dam2.jetpack.proyectofinal.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByFirebaseUidUseCase
import dam2.jetpack.proyectofinal.user.presentation.state.UserStateUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val getUserByFirebaseUidUseCase: GetUserByFirebaseUidUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(UserStateUi())
    val uiState: StateFlow<UserStateUi> = _uiState.asStateFlow()

    fun getUserByFirebaseUid(firebaseUid: String){
        viewModelScope.launch{
            _uiState.value = UserStateUi(isLoading = true)

            val result = getUserByFirebaseUidUseCase(firebaseUid)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(user = it, error = null)
                },
                onFailure = { e ->
                    _uiState.value.copy(user = null, error = e.message)
                }
            )
        }
    }
}