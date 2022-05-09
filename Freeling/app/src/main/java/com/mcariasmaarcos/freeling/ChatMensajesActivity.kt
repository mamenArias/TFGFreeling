package com.mcariasmaarcos.freeling

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.MensajeChat
import com.mcariasmaarcos.freeling.databinding.ActivityChatMensajesBinding
import com.mcariasmaarcos.freeling.databinding.ActivityMainBinding
import com.mcariasmaarcos.recycler.RecyclerMensajeAdapter

class ChatMensajesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatMensajesBinding.inflate(layoutInflater)}

    private var chatId = ""
    private var usuario = ""

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatId = intent.getStringExtra("chatId").toString()
        usuario = intent.getStringExtra("usuario").toString()

        if(chatId.isNotEmpty()){
            initViews()
        }
    }

    private fun initViews(){
        val adapter = RecyclerMensajeAdapter(usuario)
        binding.recyclerMensajesChat.adapter = adapter
        binding.recyclerMensajesChat.layoutManager = LinearLayoutManager(this)

        binding.botonEnviarMensaje.setOnClickListener {
            enviarMensaje()
        }

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("mensajes").orderBy("dob", Query.Direction.ASCENDING)
            .get().addOnSuccessListener { mensajes ->
                val listaMensajes = mensajes.toObjects(MensajeChat::class.java)
                (binding.recyclerMensajesChat.adapter as RecyclerMensajeAdapter).setData(listaMensajes)
            }

        chatRef.collection("mensajes").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { mensajes, error ->
                if (error == null){
                    mensajes?.let {
                        val listaMensajes = it.toObjects(MensajeChat::class.java)
                        (binding.recyclerMensajesChat.adapter as RecyclerMensajeAdapter).setData(listaMensajes)
                    }
                }
            }
    }

    private fun enviarMensaje(){
        val mensaje = MensajeChat(
            mensaje = binding.campoEscribirMensaje.text.toString(),
            from = usuario
        )

        db.collection("chats").document(chatId).collection("mensajes").document().set(mensaje)

        binding.campoEscribirMensaje.setText("")
    }
}