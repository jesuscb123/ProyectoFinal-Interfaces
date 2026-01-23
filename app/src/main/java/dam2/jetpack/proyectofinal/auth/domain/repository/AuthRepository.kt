package dam2.jetpack.proyectofinal.auth.domain.repository

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult


/**
 * Define el contrato (interfaz) para el repositorio de autenticación.
 *
 * Esta interfaz abstrae las operaciones de autenticación de su implementación concreta (en este caso, Firebase).
 * Al definir este contrato en la capa de dominio, permitimos que los casos de uso (UseCases) y ViewModels
 * dependan de esta abstracción en lugar de una implementación directa, siguiendo los principios
 * de la Arquitectura Limpia y la Inversión de Dependencias.
 *
 * La implementación real de esta interfaz ([dam2.jetpack.proyectofinal.auth.data.repository.AuthRepositoryImpl])
 * se encuentra en la capa de datos.
 */
interface AuthRepository {

    /**
     * Inicia sesión de un usuario utilizando su correo electrónico y contraseña.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Un objeto [AuthResult] que contiene los datos del usuario si la autenticación es exitosa.
     * @throws Exception Si la autenticación falla (ej: credenciales incorrectas, usuario no encontrado).
     */
    suspend fun login(email: String, password: String): AuthResult

    /**
     * Registra un nuevo usuario con un correo electrónico y una contraseña.
     *
     * @param email El correo electrónico para la nueva cuenta.
     * @param password La contraseña para la nueva cuenta.
     * @return Un objeto [AuthResult] que contiene los datos del nuevo usuario si el registro es exitoso.
     * @throws Exception Si el registro falla (ej: el correo ya está en uso, contraseña débil).
     */
    suspend fun register(email: String, password: String): AuthResult
    /**
     * Cierra la sesión del usuario actualmente autenticado.
     * Es una operación suspendida para permitir posibles operaciones asíncronas de limpieza si fueran necesarias.
     */
    suspend fun logOut()
    /**
     * Obtiene el Identificador Único (UID) del usuario que tiene la sesión activa actualmente.
     *
     * @return Un [String] con el UID del usuario.
     * @throws Exception Si no hay ningún usuario con la sesión iniciada.
     */
    fun getCurrentUserUid(): String
}