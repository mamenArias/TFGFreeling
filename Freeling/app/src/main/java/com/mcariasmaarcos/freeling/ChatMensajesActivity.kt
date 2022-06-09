package com.mcariasmaarcos.freeling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.MensajeChat
import com.mcariasmaarcos.freeling.databinding.ActivityChatMensajesBinding
import com.mcariasmaarcos.recycler.RecyclerMensajeAdapter

/**
 * Clase que va a recoger de los datos que se han pasado por bundle, el id del chat y el usuario conectado, para así traer sus mensajes
 * guardados en dicho chat.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class ChatMensajesActivity : AppCompatActivity() {
    /** Constante que permite enlazar los elementos del layout **/
    private val binding by lazy { ActivityChatMensajesBinding.inflate(layoutInflater)}
    /** Variable a la que le vamos a asignar el chat Id por bundle **/
    private var chatId = ""
    /** Variable a la que le vamos a asignar el email del usuario por bundle **/
    private var usuario = ""
    /** Constante para establecer la conexión a Firebase **/
    private val db = Firebase.firestore

    /**
     * Función en la que se va a obtener los datos pasados por bundle de la pantalla anterior, y si no son nulos,
     * se llamará a la función [initViews].
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("usuario")?.let { usuario = it }

        if(chatId.isNotEmpty() && usuario.isNotEmpty()){
            initViews()
        }
    }

    /**
     * Función que va a mostrar el layout de la pantalla de chat, donde veremos los mensajes que contiene dicho chat,
     * el campo donde escribir los nuevos mensajes, y el botón para enviar el mensaje.
     */
    private fun initViews(){
        binding.recyclerMensajesChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerMensajesChat.adapter = RecyclerMensajeAdapter(usuario)

        /** Botón para enviar un nuevo mensaje al chat **/
        binding.botonEnviarMensaje.setOnClickListener { enviarMensaje() }

        val chatRef = db.collection("Chats").document(chatId)
        chatRef.collection("Mensajes").orderBy("fecha", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { mensajes ->
                val listaMensajes = mensajes.toObjects(MensajeChat::class.java)
                (binding.recyclerMensajesChat.adapter as RecyclerMensajeAdapter).setData(listaMensajes)
            }

        chatRef.collection("Mensajes").orderBy("fecha", Query.Direction.ASCENDING)
            .addSnapshotListener { mensajes, error ->
                if (error == null){
                    mensajes?.let {
                        val listaMensajes = it.toObjects(MensajeChat::class.java)
                        (binding.recyclerMensajesChat.adapter as RecyclerMensajeAdapter).setData(listaMensajes)
                    }
                }
            }
    }

    /**
     * Función que va a crear un nuevo objetio MensajeChat, con el mensaje escrito y el usuario que lo envía, y lo va
     * a añadir a Firebase. Además, dejará el campo de escribir mensaje vacío después de enviarlo.
     */
    private fun enviarMensaje(){
        val mensaje = MensajeChat(
            mensaje = binding.campoEscribirMensaje.text.toString(),
            from = usuario
        )

        db.collection("Chats").document(chatId).collection("Mensajes").document().set(mensaje)

        binding.campoEscribirMensaje.setText("")
    }
}