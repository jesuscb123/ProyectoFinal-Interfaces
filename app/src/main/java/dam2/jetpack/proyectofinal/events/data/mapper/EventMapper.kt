package dam2.jetpack.proyectofinal.events.data.mapper

import dam2.jetpack.proyectofinal.events.data.local.entity.EventEntity
import dam2.jetpack.proyectofinal.events.domain.model.Event

/**
 * Archivo que contiene las funciones de extensión para mapear (convertir) objetos
 * entre la capa de datos (data) y la capa de dominio (domain) para los eventos.
 */

/**
 * Convierte un objeto de entidad de base de datos [EventEntity] a un objeto del modelo de dominio [Event].
 *
 * Este mapeador se utiliza para transformar los datos recuperados de la base de datos local
 * a un formato que la lógica de negocio y la UI puedan utilizar, desacoplando así las capas.
 *
 * @return Una instancia de [Event] con los datos de la entidad.
 */
fun EventEntity.toDomain(): Event{
    return Event(
        eventId = eventId,
        userId = userId,
        creatorUid = creatorUid,
        tituloEvento = tituloEvento,
        descripcionEvento = descripcionEvento,
        categoria = categoria,
        fechaCreacion = fechaCreacion,
        resuelto = resuelto,
        userAccept = userAccept,
        userAcceptUid = userAcceptUid
    )
}

/**
 * Convierte un objeto del modelo de dominio [Event] a un objeto de entidad de base de datos [EventEntity].
 *
 * Este mapeador se utiliza para transformar los datos del dominio a un formato que pueda ser
 * almacenado en la base de datos local de Room.
 *
 * @return Una instancia de [EventEntity] lista para ser insertada o actualizada en la base de datos.
 */
fun Event.toEntity(): EventEntity{
    return EventEntity(
        eventId = eventId,
        userId = userId,
        creatorUid = creatorUid,
        tituloEvento = tituloEvento,
        descripcionEvento = descripcionEvento,
        categoria = categoria,
        fechaCreacion = fechaCreacion,
        resuelto = resuelto,
        userAccept = userAccept,
        userAcceptUid = userAcceptUid
    )
}
