package com.mcariasmaarcos.clases

data class Usuario(var email:String="",
                   var nombre:String,var pronombre:String, var genero:String,var orientacionSexual:String,
                   var edad:Int,var edadDeseadaInf:Int,var edadDeseadaSup:Int,var biografia:String
                   ,var fotoPerfil:String,var usuariosEncontrados :ArrayList<String> = arrayListOf() ){
    constructor():this("","","","","",0,0,0,"","")

}