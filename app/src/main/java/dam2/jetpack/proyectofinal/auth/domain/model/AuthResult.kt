package dam2.jetpack.proyectofinal.auth.domain.model

import dam2.jetpack.proyectofinal.user.domain.model.Rol

/**
 * Modelo de datos que encapsula el resultado de una operación de autenticación exitosa.
 *
 * Esta clase se utiliza para transportar la información esencial de un usuario
 * inmediatamente después de que ha iniciado sesión o se ha registrado correctamente.
 * Sirve como un objeto de transferencia de datos (DTO) entre la capa de datos
 * (repositorio) y la capa de dominio o presentación.
 *
 * @property uid El identificador único (UID) del usuario proporcionado por Firebase Authentication.
 *             Este es el ID principal y más fiable para identificar a un usuario en el sistema.
 * @property email El correo electrónico asociado a la cuenta del usuario.
 *                 Es opcional (`?`) para mantener la consistencia con el modelo de usuario de Firebase,
 *                 aunque en la práctica de esta aplicación siempre se espera un valor.
 */
data class AuthResult(
    val uid: String,
    val email: String?
)
