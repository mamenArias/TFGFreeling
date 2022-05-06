package com.mcariasmaarcos.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.clases.MensajeChat
import com.mcariasmaarcos.freeling.R

class RecyclerMensajeAdapter(private val usuario:String): RecyclerView.Adapter<RecyclerMensajeHolder>() {

    private var mensajes: List<MensajeChat> = emptyList()

    fun setData(list: List<MensajeChat>){
        mensajes = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerMensajeHolder {
        return RecyclerMensajeHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.layout_recycler_mensajeschat, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerMensajeHolder, position: Int) {
        val mensaje = mensajes[position]

        if(usuario == mensaje.from){
            holder.misMensajesLayout.visibility = View.VISIBLE
            holder.otrosMensajesLayout.visibility = View.GONE

            holder.miMensajeChat.text = mensaje.mensaje
        } else {
            holder.misMensajesLayout.visibility = View.GONE
            holder.otrosMensajesLayout.visibility = View.VISIBLE

            holder.otroMensajeChat.text = mensaje.mensaje
        }
    }

    override fun getItemCount(): Int {
        return mensajes.size
    }
}