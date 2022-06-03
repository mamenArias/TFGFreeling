package com.mcariasmaarcos.clases

/**
 * Clase que modela los objetos de tipo Chat, que tendrá cada usuario.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 * @param id Id del chat creado
 * @param users Lista de usuarios de comparten el chat
 * @param nombreUsuario Nombre del usuario del chat
 */
class Chat (
    var id: String = "",
    var users: List<String> = emptyList(),
    var nombreUsuario: String? = ""
)