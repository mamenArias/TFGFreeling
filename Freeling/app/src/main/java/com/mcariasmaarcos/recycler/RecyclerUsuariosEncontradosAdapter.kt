package com.mcariasmaarcos.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Chat
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.ChatMensajesActivity
import com.mcariasmaarcos.freeling.R
import java.util.*
import kotlin.collections.ArrayList

class RecyclerUsuariosEncontradosAdapter(private val context: Context?, private var usuariosEncontrados: ArrayList<String>): RecyclerView.Adapter<RecyclerUsuariosEncontradosHolder>(){

    private val db = Firebase.firestore
    var otroUsuarioEmail: String? = null
    lateinit var usuarioActual:Usuario

    fun setData(list: ArrayList<String>){
        usuariosEncontrados = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerUsuariosEncontradosHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_usuariosencontrados, parent, false)
        return RecyclerUsuariosEncontradosHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerUsuariosEncontradosHolder, position: Int) {

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
            .get() //Al debuguear, se detiene en esta linea y salta hasta el return, dando como resultado un 0 cuando se recibe en el adaptador.
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    usuarioActual = it.result.toObject(Usuario::class.java)!!
                }

                db.collection("Usuarios").document(usuariosEncontrados[position]).get()
                    .addOnSuccessListener {
                        Glide.with(context!!).load(it.get("fotoPerfil")).into(holder.fotoUsuario)
                        holder.fotoUsuario.scaleType = ImageView.ScaleType.CENTER_CROP
                        holder.nombreUsuario.text = it.get(("nombre")).toString()
                        holder.pronombreUsuario.text = it.get("pronombre").toString()
                        holder.edadUsuario.text = it.get("edad").toString()
                        otroUsuarioEmail = it.get("email").toString()
                    }
            }

        holder.bontonAceptar.setOnClickListener {
            val chatId = UUID.randomUUID().toString()
            val otroUsuario = otroUsuarioEmail
            val usuariosChat = listOf<String>(usuarioActual.email, otroUsuario.toString())

            val chat: Chat = Chat(chatId, "Chat con $otroUsuario", usuariosChat)

            db.collection("chats").document(chatId).set(chat)
            db.collection("Usuarios").document(usuarioActual.email).collection("chats").document(chatId).set(chat)
            if (otroUsuario != null) {
                db.collection("Usuarios").document(otroUsuario).collection("chats").document(chatId).set(chat)
            }
        }
    }

    override fun getItemCount(): Int {
        return usuariosEncontrados.size
    }
}