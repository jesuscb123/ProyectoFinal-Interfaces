package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogOut @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(){
        authRepository.logOut()
    }
}