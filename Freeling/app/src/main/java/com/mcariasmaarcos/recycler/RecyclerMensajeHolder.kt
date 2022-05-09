package com.mcariasmaarcos.recycler

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.freeling.R

class RecyclerMensajeHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    val misMensajesLayout:View by lazy { itemView.findViewById(R.id.misMensajesLayout) }
    val miMensajeChat:TextView by lazy { itemView.findViewById(R.id.miMensajeTexto) }
    val otrosMensajesLayout:View by lazy { itemView.findViewById(R.id.otrosMensajesLayout) }
    val otroMensajeChat:TextView by lazy { itemView.findViewById(R.id.otroMensajeTexto) }

}