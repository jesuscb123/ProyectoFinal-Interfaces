package dam2.jetpack.proyectofinal.user.data.local.converter

import androidx.room.TypeConverter
import dam2.jetpack.proyectofinal.user.domain.model.Rol

class RolConverter {
    @TypeConverter fun fromRol(rol: Rol): String = rol.name
    @TypeConverter fun toRol(rol: String): Rol = Rol.valueOf(rol)
}