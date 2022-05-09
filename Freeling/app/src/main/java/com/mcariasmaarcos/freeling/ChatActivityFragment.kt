package com.mcariasmaarcos.freeling


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
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentChatActivityBinding
import com.mcariasmaarcos.recycler.RecyclerChatAdapter
import com.mcariasmaarcos.recycler.RecyclerUsuariosEncontradosAdapter

private lateinit var binding: FragmentChatActivityBinding

private val db = FirebaseFirestore.getInstance()

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
        lateinit var user: Usuario
        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user = it.result.toObject(Usuario::class.java)!!
                }
            }.addOnSuccessListener {
                if(user!=null){
                    listaChats = user!!.listaChats
                    val adapter: RecyclerChatAdapter = RecyclerChatAdapter(this.context,listaChats)
                    binding.recyclerChats.adapter = adapter
                    binding.recyclerChats.layoutManager = LinearLayoutManager(this.context)
                }
            }
    }
}