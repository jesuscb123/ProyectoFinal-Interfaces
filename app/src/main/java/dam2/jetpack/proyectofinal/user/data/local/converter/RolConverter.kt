package dam2.jetpack.proyectofinal.user.data.local.converter

import androidx.room.TypeConverter
import dam2.jetpack.proyectofinal.user.domain.model.Rol

/**
 * Convertidor de tipos para la base de datos Room que se encarga de la
 * serialización y deserialización del enum [Rol].
 *
 * Room no sabe cómo almacenar tipos de datos complejos como un enum por defecto,
 * por lo que este convertidor le indica cómo guardar el enum como un [String]
 * en la base de datos y cómo convertirlo de nuevo a un objeto [Rol] al leerlo.
 */
class RolConverter {
    /**
     * Convierte una instancia del enum [Rol] a su representación en [String] (su nombre).
     *
     * @param rol El valor del enum [Rol] a convertir.
     * @return El nombre del enum como un [String] para ser almacenado en la base de datos.
     */
    @TypeConverter
    fun fromRol(rol: Rol): String = rol.name

    /**
     * Convierte un [String] (el nombre del enum) de nuevo a una instancia de [Rol].
     *
     * @param rol El [String] recuperado de la base de datos.
     * @return La instancia correspondiente del enum [Rol].
     */
    @TypeConverter
    fun toRol(rol: String): Rol = Rol.valueOf(rol)
}
