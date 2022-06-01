package com.mcariasmaarcos.recycler

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.freeling.R

/**
 * ViewHolder personalizado para el RecyclerView de los usuarios encontrados a través de Google Nearby,
 * que contiene los elementos del layout del recycler.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.1
 * @param itemView Elemento del layout del recycler
 */
class RecyclerUsuariosEncontradosHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    /** ImageView que contiene la foto de perfil del usuario encontrado **/
    val fotoUsuario:ImageView by lazy { itemView.findViewById(R.id.fotoUsuarioEncontradoChat) }
    /** TextView que contiene el nombre del usuario encontrado **/
    val nombreUsuario:TextView by lazy { itemView.findViewById(R.id.nombreUsuarioEncontradoChat) }
    /** TextView que contiene el pronombre del usuario encontrado **/
    val pronombreUsuario:TextView by lazy { itemView.findViewById(R.id.pronombreUsuarioEncontrado) }
    /** TextView que contiene la edad del usuario encontrado **/
    val edadUsuario:TextView by lazy { itemView.findViewById(R.id.edadUsuarioEncontrado) }
    /** ImageButton con el que vamos a aceptar al usuario encontrado para añadirlo a la pantalla de chats **/
    val bontonAceptar:ImageButton by lazy { itemView.findViewById(R.id.botonAceptarUsuario) }
    /** ImageButton con el que vamos a rechazar al usuario encontrado y se eliminará del recycler **/
    val botonRechazar:ImageButton by lazy { itemView.findViewById(R.id.botonRechazarUsuario) }

}