package com.mcariasmaarcos.clases

import java.util.*

/**
 * Clase que modela los objetos de tipo MensajeChat, que tendrá cada chat.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 * @param mensaje Mensaje enviado al chat
 * @param from Usuario que envía el mensaje
 * @param fecha Fecha de envío del mensaje
 */
class MensajeChat (
    var mensaje: String = "",
    var from: String = "",
    var fecha: Date = Date()
)