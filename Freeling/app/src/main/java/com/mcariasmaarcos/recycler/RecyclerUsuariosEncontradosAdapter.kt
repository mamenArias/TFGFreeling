package com.mcariasmaarcos.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Chat
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.PerfilUsuarioEncontradoActivity
import com.mcariasmaarcos.freeling.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter para el Recycler de los usuarios encontrados a través de Google Nearby
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.2
 * @param context Contexto donde se implementa el Recycler
 * @param usuariosEncontrados ArrayList de los usuarios encontrados que se añadirán al recycler
 */
class RecyclerUsuariosEncontradosAdapter(private val context: Context?, private var usuariosEncontrados: ArrayList<String>): RecyclerView.Adapter<RecyclerUsuariosEncontradosHolder>(){

    /** Constante para establecer la conexión a Firebase **/
    private val db = Firebase.firestore
    /** Variable que se inicializará con el usuario actual conectado una vez que accedamos a Firebase **/
    lateinit var usuarioActual:Usuario

    /**
     * Función que va a igualar la lista de usuarios encontrados a la que se le pase por argumentos.
     * @param list Lista de usuarios encontrados
     */
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
                        holder.nombreUsuario.setText(it.get(("nombre")).toString())
                        holder.pronombreUsuario.setText(it.get("pronombre").toString())
                        holder.edadUsuario.setText(it.get("edad").toString())

                        holder.bontonAceptar.setOnClickListener {

                            /** Id del chat que vamos a crear generada aleatoriamente **/
                            val chatId = UUID.randomUUID().toString()
                            /** Lista de usuarios que utilizan el chat que se va a crear **/
                            val usuariosChat = listOf<String>(usuarioActual.email, usuariosEncontrados[position])

                            /** Chat creado con los dos usuarios **/
                            val chat = Chat(chatId, usuariosChat, holder.nombreUsuario.text.toString())

                            db.collection("Chats").document(chatId).set(chat)
                            db.collection("Usuarios").document(usuarioActual.email).collection("Chats").document(chatId).set(chat)
                            //if (otroUsuario != null) {
                            db.collection("Usuarios").document(usuariosEncontrados[position]).collection("Chats").document(chatId).set(chat)
                            //}
                            /*db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
                                .update("listaChats", FieldValue.arrayUnion(otroUsuario))*/
                            /*usuariosEncontrados.removeAt(position)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()
                            db.collection("Usuarios").document(usuariosEncontrados[position]).delete()
                                .addOnSuccessListener { Log.d(TAG, "Usuario añadido a la lista de chats") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error al borrar el usuario") }*/
                        }

                        /*holder.botonRechazar.setOnClickListener {
                            usuariosEncontrados.removeAt(position)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()
                            db.collection("Usuarios").document(usuarioActual.email).get(usuariosEncontrados[position]).delete()
                                //.addOnSuccessListener { Log.d(TAG, "Usuario eliminado") }
                                //.addOnFailureListener { e -> Log.w(TAG, "Error al borrar el usuario") }
                        }*/
                    }
            }

        /** Al hacer click sobre cualquier elemento del recycler, nos llevará a la pantalla PerfilUsuarioEncontrado,
         * y nos mostrará los datos de dicho usuario **/
        holder.itemView.setOnClickListener {
            val intent = Intent(this.context, PerfilUsuarioEncontradoActivity::class.java)
            intent.putExtra("otroUsuario", usuariosEncontrados[position])
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return usuariosEncontrados.size
    }
}