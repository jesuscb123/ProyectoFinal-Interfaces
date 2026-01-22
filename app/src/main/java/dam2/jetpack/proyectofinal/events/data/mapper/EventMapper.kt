package dam2.jetpack.proyectofinal.events.data.mapper

import dam2.jetpack.proyectofinal.events.data.local.entity.EventEntity
import dam2.jetpack.proyectofinal.events.domain.model.Event

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