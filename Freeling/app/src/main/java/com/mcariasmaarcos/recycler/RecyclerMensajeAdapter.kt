package com.mcariasmaarcos.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcariasmaarcos.clases.MensajeChat
import com.mcariasmaarcos.freeling.R

/**
 * Adapter para el Recycler de los mensajes de los chats.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 * @param usuario Usuario con el que se mantiene la conversación del chat.
 */
class RecyclerMensajeAdapter(private val usuario:String): RecyclerView.Adapter<RecyclerMensajeHolder>() {

    /** Lista de mensajes del chat **/
    private var mensajes: List<MensajeChat> = emptyList()

    /**
     * Función que va a igualar la lista de mensajes del adapter, a la que se le pase por argumentos.
     * @param list Lista de mensajes que recibe el chat
     */
    fun setData(list: List<MensajeChat>){
        mensajes = list
        notifyDataSetChanged()
    }

    /**
     * Función que infla el adapter.
     * @param parent vista padre que contendrá el recycler
     * @param viewType tipos de vista que puede haber en el layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerMensajeHolder {
        return RecyclerMensajeHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.layout_recycler_mensajeschat, parent, false))
    }

    /**
     * Función que según quién de las 2 personas del chat hable, mostrará el mensaje en un lado u otro de la pantalla,
     * y de un color u otro.
     * @param holder holder del adapter del recycler
     * @param position posición del array del elemento.
     */
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

    /**
     * Función que devuelve el tamaño del array de mensajes.
     */
    override fun getItemCount(): Int {
        return mensajes.size
    }
}