package dam2.jetpack.proyectofinal.events.domain.usecase
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener las estadísticas de los eventos.
 *
 * Este caso de uso encapsula la lógica para solicitar las estadísticas de eventos
 * (completados y aceptados) desde el [EventRepository]. Al actuar como intermediario,
 * mantiene el ViewModel (capa de presentación) desacoplado de los detalles de
 * la capa de datos.
 *
 * @param eventRepository El repositorio que proporciona acceso a los datos de los eventos.
 */
class GetEventStatsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso para obtener las estadísticas.
     * La sobrecarga del operador `invoke` permite llamar a esta clase como si fuera una función.
     *
     * @return Un objeto [Result] que encapsula el resultado de la operación:
     *         - `Result.success(Pair<Int, Int>)`: Si la operación es exitosa, contiene un par
     *           de enteros (completados, aceptados).
     *         - `Result.failure(Exception)`: Si ocurre un error durante la consulta a la base de datos.
     */
    suspend operator fun invoke(): Result<Pair<Int, Int>> {
        return eventRepository.getEventStats()
    }
}
