package com.mcariasmaarcos.freeling

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Chat
import com.mcariasmaarcos.freeling.databinding.FragmentChatActivityBinding
import com.mcariasmaarcos.recycler.RecyclerChatAdapter

/** Variable que va a almacenar el email del usuario conectado a la app **/
lateinit var user: String

/**
 * Fragment que obtiene la lista de usuarios que ha aceptado el usuario, con los que puede iniciar un chat.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.2
 */
class ChatActivityFragment : Fragment(R.layout.fragment_chat_activity) {

    /** Variable que permite enlazar los elementos del layout **/
    private lateinit var binding: FragmentChatActivityBinding
    /** Constante para establecer la conexión a Firebase **/
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = FragmentChatActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var listaChats:ArrayList<String>

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get()
            .addOnSuccessListener {
                user = it.get("email").toString()
            }

        /*db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user = it.result.toObject(Usuario::class.java)!!
                }
            }.addOnSuccessListener {
                if(user!=null){
                    listaChats = user!!.listaChats
                    val adapter = RecyclerChatAdapter(this.context,listaChats)
                    binding.recyclerChats.adapter = adapter
                    binding.recyclerChats.layoutManager = LinearLayoutManager(this.context)
                }
            }*/

        binding.recyclerChats.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerChats.adapter = RecyclerChatAdapter {chat: Chat ->  chatSeleccionado(chat)}

        /** Documento de Firebase al que accedemos para traer la colección Chats que hay dentro del mismo **/
        val userRef = db.collection("Usuarios").document(user)

        userRef.collection("Chats").get()
            .addOnSuccessListener { chats ->
                val listaChats = chats.toObjects(Chat::class.java)
                (binding.recyclerChats.adapter as RecyclerChatAdapter).setData(listaChats)
            }

        userRef.collection("Chats").addSnapshotListener { chats, error ->
            if (error == null){
                chats?.let {
                    val listaChats = it.toObjects(Chat::class.java)
                    (binding.recyclerChats.adapter as RecyclerChatAdapter).setData(listaChats)
                }
            }
        }
    }

    /**
     * Función para seleccionar un chat en concreto de la lista, y pasar por Bundle el id de dicho chat y el email del usuario
     * que accede a él.
     * @param chat Chat seleccionado al que accedemos para recuperar sus mensajes
     */
    private fun chatSeleccionado(chat:Chat){
        val intent = Intent(this.context, ChatMensajesActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("usuario", user)
        startActivity(intent)
    }
}