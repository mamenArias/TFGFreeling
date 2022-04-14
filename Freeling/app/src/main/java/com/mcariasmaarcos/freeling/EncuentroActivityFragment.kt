package com.mcariasmaarcos.freeling

import android.content.ClipData.Item
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentEncuentroActivityBinding
import com.mcariasmaarcos.recycler.RecyclerUsuariosEncontradosAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [EncuentroActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EncuentroActivityFragment : Fragment(R.layout.fragment_encuentro_activity) {

    private lateinit var binding:FragmentEncuentroActivityBinding
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEncuentroActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listaUsuarios:ArrayList<String> = arrayListOf<String>()
       // listaUsuarios.add("moneite@gmail.com")
        //listaUsuarios.add("mamen@gmail.com")
       lateinit var user:Usuario
       db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
            .get() //Al debuguear, se detiene en esta linea y salta hasta el return, dando como resultado un 0 cuando se recibe en el adaptador.
            .addOnCompleteListener {
                if (it.isSuccessful) {
                     user = it.result.toObject(Usuario::class.java)!!
                  //  adapter.notifyDataSetChanged() //le avisamos al adapter que tiene nuevos elementos
                }
            }.addOnSuccessListener {
                if(user!=null){
                    //Toast.makeText(this.context, "llegue", Toast.LENGTH_SHORT).show()
                    listaUsuarios = user!!.usuariosEncontrados
                    val adapter:RecyclerUsuariosEncontradosAdapter = RecyclerUsuariosEncontradosAdapter(this.context,listaUsuarios)
                    binding.recyclerPersonasEncontradas.adapter = adapter
                    binding.recyclerPersonasEncontradas.layoutManager = LinearLayoutManager(this.context)
                }
           }


    }

}

