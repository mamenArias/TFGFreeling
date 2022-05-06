package com.mcariasmaarcos.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.R

class RecyclerUsuariosEncontradosAdapter(private val context: Context?, private var usuariosEncontrados: ArrayList<String>): RecyclerView.Adapter<RecyclerUsuariosEncontradosHolder>(){

    private val db = Firebase.firestore

    fun setData(list: ArrayList<String>){
        usuariosEncontrados = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerUsuariosEncontradosHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_usuariosencontrados, parent, false)
        return RecyclerUsuariosEncontradosHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerUsuariosEncontradosHolder, position: Int) {

        db.collection("Usuarios").document(usuariosEncontrados[position]).get().addOnSuccessListener {
            Glide.with(context!!).load(it.get("fotoPerfil")).into(holder.fotoUsuario)
            holder.fotoUsuario.scaleType = ImageView.ScaleType.CENTER_CROP
            holder.nombreUsuario.text = it.get(("nombre")).toString()
            holder.pronombreUsuario.text = it.get("pronombre").toString()
            holder.edadUsuario.text = it.get("edad").toString()
        }

    }

    override fun getItemCount(): Int {
        return usuariosEncontrados.size
    }
}