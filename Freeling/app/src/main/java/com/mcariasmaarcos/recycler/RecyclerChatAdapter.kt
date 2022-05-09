package com.mcariasmaarcos.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.freeling.ChatMensajesActivity
import com.mcariasmaarcos.freeling.R

class RecyclerChatAdapter(private val context: Context?, private val listaChats: ArrayList<String>): RecyclerView.Adapter<RecyclerChatHolder>() {

    private val db = Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerChatHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_chatlist, parent, false)
        return RecyclerChatHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerChatHolder, position: Int) {
        db.collection("Usuarios").document(listaChats[position]).get().addOnSuccessListener {
            Glide.with(context!!).load(it.get("fotoPerfil")).into(holder.fotoUsuarioChat)
            holder.fotoUsuarioChat.scaleType = ImageView.ScaleType.CENTER_CROP
            holder.nombreUsuarioChat.text = it.get(("nombre")).toString()
        }

        holder.itemView.setOnClickListener {
            listaChats[position]
            val intent = Intent(context, ChatMensajesActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaChats.size
    }
}