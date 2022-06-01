package com.mcariasmaarcos.clases

/**
 * Clase que modela los objetos de tipo Usuario.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.2
 * @param email Email del usuario.
 * @param nombre Nombre del usuario.
 * @param pronombre Pronombre del usuario.
 * @param genero Género del usuario
 * @param orientacionSexual Orientación sexual del usuario.
 * @param edad Edad del usuario.
 * @param edadDeseadaInf Edad mínima de la gente que busca el usuario para conocer.
 * @param edadDeseadaSup Edad máxima de la gente que busca el usuario para conocer.
 * @param biografia Datos de interés del usuario, como qué le gusta hacer, etc.
 * @param fotoPerfil Foto de perfil del usuario.
 * @param medallasBuenas Medallas buenas que ha recibido el usuario
 * @param medallasMalas Medallas malas que ha recibido el usuario
 * @param usuariosEncontrados Lista de usuarios que ha encontrado el usuario a través de Google Nearby
 */
data class Usuario(var email:String="",
                   var nombre:String,var pronombre:String, var genero:String,var orientacionSexual:String,
                   var edad:Int,var edadDeseadaInf:Int,var edadDeseadaSup:Int,var biografia:String
                   ,var fotoPerfil:String, var medallasBuenas:Int, var medallasMalas:Int,
                   var usuariosEncontrados :ArrayList<String> = arrayListOf()
                    /*var listaChats:ArrayList<String> = arrayListOf()*/){
    constructor():this("","","","","",0,0,0,"","",0,0)

}