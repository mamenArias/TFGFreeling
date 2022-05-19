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
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentChatActivityBinding
import com.mcariasmaarcos.recycler.RecyclerChatAdapter

private lateinit var binding: FragmentChatActivityBinding

private val db = FirebaseFirestore.getInstance()
lateinit var user: String

class ChatActivityFragment : Fragment(R.layout.fragment_chat_activity) {

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listaChats:ArrayList<String>

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get() //Al debuguear, se detiene en esta linea y salta hasta el return, dando como resultado un 0 cuando se recibe en el adaptador.
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

    private fun chatSeleccionado(chat:Chat){
        val intent = Intent(this.context, ChatMensajesActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("usuario", user)
        startActivity(intent)
    }
}