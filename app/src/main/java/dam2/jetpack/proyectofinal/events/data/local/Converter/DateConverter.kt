package dam2.jetpack.proyectofinal.events.data.local.Converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * Convertidor de tipos para la base de datos Room.
 *
 * Esta clase le indica a Room cómo convertir un objeto [Date] a un formato que pueda
 * ser almacenado en la base de datos (un [Long] que representa el timestamp) y viceversa.
 * Maneja valores nulos para asegurar que las fechas opcionales se puedan guardar correctamente.
 */
class DateConverter {

    /**
     * Convierte un objeto [Date] a su representación de timestamp en milisegundos ([Long]).
     *
     * Room usará este método para guardar fechas en la base de datos.
     *
     * @param date El objeto [Date] a convertir. Puede ser nulo.
     * @return El timestamp en milisegundos como [Long], o nulo si la fecha de entrada es nula.
     */
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    /**
     * Convierte un timestamp en milisegundos ([Long]) de vuelta a un objeto [Date].
     *
     * Room usará este método para leer fechas de la base de datos.
     *
     * @param timestamp El valor [Long] del timestamp leído de la base de datos. Puede ser nulo.
     * @return El objeto [Date] correspondiente, o nulo si el timestamp de entrada es nulo.
     */
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
