package com.mcariasmaarcos.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Chat
import com.mcariasmaarcos.freeling.R
import com.mcariasmaarcos.freeling.user
import com.squareup.picasso.Picasso

/**
 * Adapter para el Recycler de la lista de chats del usuario.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 * @param chatClick función que nos va a llevar al chat sobre el que hagamos click en el recycler
 */
class RecyclerChatAdapter( private val context: Context?,val chatClick: (Chat) -> Unit/*, private val listaChats: ArrayList<String>*/): RecyclerView.Adapter<RecyclerChatHolder>() {
    /** Lista de chats **/
    var chats: List<Chat> = emptyList()

    /** Constante para establecer la conexión a Firebase **/
    private val db = Firebase.firestore

    /**
     * Función que va a igualar la lista de chats del adapter, a la que se le pase por argumentos.
     * @param list Lista de chats que recibe el usuario
     */
    fun setData(list: List<Chat>){
        chats = list
        notifyDataSetChanged()
    }

    /**
     * Función que infla el adapter.
     * @param parent vista padre que contendrá el recycler
     * @param viewType tipos de vista que puede haber en el layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerChatHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_chatlist, parent, false)
        return RecyclerChatHolder(view)
    }

    /**
     * Función que va a cargar los diferentes chats que tenga el usuario en su base de datos, y a través de los cuales,
     * pinchando sobre ellos, podremos acceder a la conversación de chat guardada para cada uno.
     * @param holder holder del adapter del recycler
     * @param position posición del array del elemento.
     */
    override fun onBindViewHolder(holder: RecyclerChatHolder, position: Int) {

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get()
            .addOnSuccessListener {
                user = it.get("email").toString()
            }

        if (chats[position].users[1].equals(user)){
            datosUsuarioChat(chats[position].users[0], holder)
        } else if (chats[position].users[0].equals(user)){
            datosUsuarioChat(chats[position].users[1], holder)
        }

        /** Elemento que al hacer click sobre él, nos llevará al chat en cuestión de la lista **/
        holder.itemView.setOnClickListener {
            chatClick(chats[position])
        }
    }

    /**
     * Función que devuelve el tamaño del array de chats.
     */
    override fun getItemCount(): Int {
        return chats.size
    }

    /**
     * Función para recoger los datos del usuario desde Firebase y añadirlo a los elementos del layout, que mostrará al otro usuario
     * en cada app.
     * Además, a través de esta función se sumarán las medallas buenas y malas al usuario y se actualizarán en la base de datos.
     * @param usuario Usuario con el que mantiene el chat
     * @param holder ViewHolder del Adapter del recycler del chat
     */
    fun datosUsuarioChat(usuario:String, holder: RecyclerChatHolder){
        db.collection("Usuarios").document(usuario).get().addOnSuccessListener {
            holder.nombreUsuarioChat.setText(it.get("nombre").toString())
            Picasso.get().load(it.get("fotoPerfil").toString()).into(holder.fotoUsuarioChat)
            holder.fotoUsuarioChat.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        holder.darMedallaBuena.setOnClickListener {
            var numMedallasBuenas:Int = 0
            db.collection("Usuarios").document(usuario).get().addOnSuccessListener {
                numMedallasBuenas = it.get("medallasBuenas").toString().toInt()
                numMedallasBuenas++
                db.collection("Usuarios").document(usuario)
                    .update("medallasBuenas", numMedallasBuenas)
                //Esto te pone la medalla a true
                db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).update("darMedallas",true)
                Toast.makeText(context, "Medalla Buena Entregada", Toast.LENGTH_SHORT).show()
            }
        }

        holder.darMedallaMala.setOnClickListener {
            var numMedallasMalas:Int = 0
            db.collection("Usuarios").document(usuario).get().addOnSuccessListener {
                numMedallasMalas = it.get("medallasMalas").toString().toInt()
                numMedallasMalas++
                db.collection("Usuarios").document(usuario)
                    .update("medallasMalas", numMedallasMalas)
                //Esto te pone la medalla a true
                db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).update("darMedallas",true)
                Toast.makeText(context, "Medalla Mala Entregada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}