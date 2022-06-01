package com.mcariasmaarcos.recycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.freeling.R

/**
 * ViewHolder personalizado para el RecyclerView de los mensajes que contiene cada chat,
 * que contiene los elementos del layout del recycler.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.1
 * @param itemView Elemento del layout del recycler
 */
class RecyclerMensajeHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    /** Layout que contiene los mensajes enviados por el usuario principal **/
    val misMensajesLayout:View by lazy { itemView.findViewById(R.id.misMensajesLayout) }
    /** TextView que contiene los mensajes enviados por el usuario principal **/
    val miMensajeChat:TextView by lazy { itemView.findViewById(R.id.miMensajeTexto) }
    /** Layout que contiene los mensajes enviados por el resto de usuarios que hablan con el usuario principal **/
    val otrosMensajesLayout:View by lazy { itemView.findViewById(R.id.otrosMensajesLayout) }
    /** TextView que contiene los mensajes recibidos del otro usuario con quien comparte el chat **/
    val otroMensajeChat:TextView by lazy { itemView.findViewById(R.id.otroMensajeTexto) }

}