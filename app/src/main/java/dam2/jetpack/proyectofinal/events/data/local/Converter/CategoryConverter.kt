package dam2.jetpack.proyectofinal.events.data.local.Converter

import androidx.room.TypeConverter
import dam2.jetpack.proyectofinal.events.domain.model.Category

/**
 * Convertidor de tipos para la base de datos Room.
 *
 * Esta clase le indica a Room cómo convertir un objeto de tipo [Category] a un formato
 * que pueda ser almacenado en la base de datos (un [String]) y viceversa.
 */
class CategoryConverter {
    /**
     * Convierte una instancia del enum [Category] a su representación en [String] (su nombre).
     *
     * @param category El objeto [Category] a convertir.
     * @return El nombre del enum como un [String].
     */
    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    /**
     * Convierte un [String] de vuelta a su correspondiente objeto del enum [Category].
     *
     * @param value El valor [String] leído de la base de datos.
     * @return El objeto [Category] correspondiente.
     */
    @TypeConverter
    fun toCategory(value: String): Category = Category.valueOf(value)
}
