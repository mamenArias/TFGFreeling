package com.mcariasmaarcos.clases

data class Usuario(var email:String="",var fotoPerfil:String,
                   var nombre:String,var pronombre:String, var genero:String,var orientacionSexual:String,
                   var edad:Byte,var edadDeseadaInf:Byte,var edadDeseadaSup:Byte,var biografia:String) {
}