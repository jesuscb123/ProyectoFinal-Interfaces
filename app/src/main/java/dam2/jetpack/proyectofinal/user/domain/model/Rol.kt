package dam2.jetpack.proyectofinal.user.domain.model

/**
 * Define los roles que puede tener un usuario dentro de la aplicación.
 *
 * Este enum se utiliza para gestionar los niveles de acceso y los permisos
 * de los usuarios en diferentes partes del sistema.
 */
enum class Rol {
    /**
     * Rol estándar para un usuario normal de la aplicación.
     */
    USER,

    /**
     * Rol de administrador con permisos elevados para gestionar la aplicación,
     * como ver todos los usuarios, modificar datos, etc.
     */
    ADMIN;
}
