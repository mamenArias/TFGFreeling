package com.mcariasmaarcos.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Chat
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.ChatMensajesActivity
import com.mcariasmaarcos.freeling.R

class RecyclerChatAdapter(val chatClick: (Chat) -> Unit/*, private val context: Context?, private val listaChats: ArrayList<String>*/): RecyclerView.Adapter<RecyclerChatHolder>() {

    private val db = Firebase.firestore
    var chats: List<Chat> = emptyList()
    //lateinit var usuarioActual:Usuario


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

        holder.emailChat.text = chats[position].users[1].toString()
        holder.nombreUsuarioChat.text = chats[position].nombreUsuario
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
}