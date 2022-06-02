package com.mcariasmaarcos.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.freeling.R

/**
 * ViewHolder personalizado para el RecyclerView de la lista de chats, que contiene los elementos del layout del recycler.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.1
 * @param itemView Elemento del layout del recycler
 */
class RecyclerChatHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    /** ImageView que muestra la foto de perfil del usuario con el que se comparte el chat **/
    val fotoUsuarioChat:ImageView by lazy { itemView.findViewById(R.id.fotoUsuarioEncontradoChat) }
    /** TextView que muestra el nombre del usuario con el que se comparte el chat **/
    val nombreUsuarioChat:TextView by lazy { itemView.findViewById(R.id.nombreUsuarioEncontradoChat) }
    /** ImageView que muestra si el usuario con el que se comparte el chat tiene una medalla buena **/
    val darMedallaBuena:ImageView by lazy { itemView.findViewById(R.id.darMedallaBuena) }
    /** ImageView que muestra si el usuario con el que se comparte el chat tiene una medalla mala **/
    val darMedallaMala:ImageView by lazy { itemView.findViewById(R.id.darMedallaMala) }

}