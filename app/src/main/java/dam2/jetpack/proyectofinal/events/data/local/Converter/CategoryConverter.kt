package dam2.jetpack.proyectofinal.events.data.local.Converter

import androidx.room.TypeConverter
import dam2.jetpack.proyectofinal.events.domain.model.Category

class CategoryConverter {
    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    @TypeConverter
    fun toCategory(value: String): Category = Category.valueOf(value)
}