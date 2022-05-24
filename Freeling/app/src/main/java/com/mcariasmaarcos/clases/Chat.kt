package com.mcariasmaarcos.clases

class Chat (
    var id: String = "",
    var users: List<String> = emptyList()
) {
    companion object{
        var id: String = ""
        var users: List<String> = emptyList()
    }
}