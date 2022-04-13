package com.mcariasmaarcos.recycler

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.freeling.R

class RecyclerUsuariosEncontradosHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val fotoUsuario:ImageView by lazy { itemView.findViewById(R.id.fotoUsuarioEncontrado) }
    val nombreUsuario:TextView by lazy { itemView.findViewById(R.id.nombreUsuarioEncontrado) }
    val pronombreUsuario:TextView by lazy { itemView.findViewById(R.id.pronombreUsuarioEncontrado) }
    val edadUsuario:TextView by lazy { itemView.findViewById(R.id.edadUsuarioEncontrado) }
    val bontonAceptar:Button by lazy { itemView.findViewById(R.id.botonAceptarUsuario) }
    val botonRechazar:Button by lazy { itemView.findViewById(R.id.botonRechazarUsuario) }

}