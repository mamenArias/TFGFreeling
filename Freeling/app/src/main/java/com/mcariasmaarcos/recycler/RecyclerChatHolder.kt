package com.mcariasmaarcos.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.freeling.R

class RecyclerChatHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    val fotoUsuarioChat:ImageView by lazy { itemView.findViewById(R.id.fotoUsuarioEncontradoChat) }
    val nombreUsuarioChat:TextView by lazy { itemView.findViewById(R.id.nombreUsuarioEncontradoChat) }
    val emailChat:TextView by lazy { itemView.findViewById(R.id.campoEmailChat)}

}