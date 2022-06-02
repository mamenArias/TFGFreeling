package com.mcariasmaarcos.recycler

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
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
 * @author Mª Carme Arias de Haro
 * @since 1.2
 * @param chatClick función que nos va a llevar al chat sobre el que hagamos click en el recycler
 */
class RecyclerChatAdapter( private val context: Context?,val chatClick: (Chat) -> Unit/*, private val listaChats: ArrayList<String>*/): RecyclerView.Adapter<RecyclerChatHolder>() {

    //private val db = Firebase.firestore
    /** Lista de chats **/
    var chats: List<Chat> = emptyList()
    /** Constante para establecer la conexión a Firebase **/
    private val db = Firebase.firestore
    //lateinit var usuarioActual:Usuario

    /**
     * Función que va a igualar la lista de chats del adapter, a la que se le pase por argumentos.
     * @param list Lista de chats que recibe el usuario
     */
    fun setData(list: List<Chat>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerChatHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_chatlist, parent, false)
        return RecyclerChatHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerChatHolder, position: Int) {

        /*var listaChats: ArrayList<String> = arrayListOf()

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    usuarioActual = it.result.toObject(Usuario::class.java)!!
                }

                listaChats = usuarioActual.listaChats
                db.collection("Usuarios").document(listaChats[position]).get()
                    .addOnSuccessListener {
                        //Glide.with(context!!).load(it.get("fotoPerfil")).into(holder.fotoUsuario)
                        //holder.fotoUsuario.scaleType = ImageView.ScaleType.CENTER_CROP
                        holder.nombreUsuarioChat.text = it.get(("nombre")).toString()
                        holder.emailChat.text = it.get("email").toString()
                    }
            }*/

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



        /*lateinit var chatId: String
        db.collection("Chats").document(Firebase.auth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener {
                chatId = it.get("id").toString()
            }

        lateinit var usuarioEmail: String
        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener {
                usuarioEmail = it.get("email").toString()
            }

        holder.itemView.setOnClickListener {
            listaChats[position]
            val intent = Intent(context, ChatMensajesActivity::class.java)
            intent.putExtra("chatId", chatId)
            intent.putExtra("usuario", usuarioEmail)
            context?.startActivity(intent)
        }*/
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    /**
     * Función para recoger los datos del usuario desde Firebase y añadirlo a los elementos del layout, que mostrará al otro usuario
     * en cada app.
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

            /*holder.darMedallaBuena.visibility = View.GONE
            holder.medallaBuenaByN.visibility = View.VISIBLE
            holder.darMedallaMala.visibility = View.GONE*/
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