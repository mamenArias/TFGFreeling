package com.mcariasmaarcos.freeling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentLogrosActivityBinding

/**
 * Fragment en el que se van a mostrar los logros conseguidos y por conseguir del usuario conectado.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carme Arias de Haro
 * @since 1.2
 */
class LogrosActivityFragment : Fragment(R.layout.fragment_logros_activity) {
    /** Variable que permite enlazar los elementos del layout **/
    private lateinit var binding: FragmentLogrosActivityBinding
    /** Constante para establecer la conexión a Firebase **/
    private val db = FirebaseFirestore.getInstance()
    //private lateinit var auth: FirebaseAuth
    //private lateinit var storageReference: StorageReference
    //val dbStorage = FirebaseStorage.getInstance()


    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = FragmentLogrosActivityBinding.inflate(inflater, container, false)
        return binding.root
        //requireActivity().intent.extras!!.getString("user")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get()
            .addOnCompleteListener {
                lateinit var user: Usuario
                if (it.isSuccessful) {
                    user = it.result.toObject(Usuario::class.java)!!
                    //adapter.notifyDataSetChanged() //le avisamos al adapter que tiene nuevos elementos
                    if (user != null) {
                        if (user.usuariosEncontrados.size > 0) {
                            binding.encuentraUnoColor.visibility = View.VISIBLE
                            binding.encuentraunobyn.visibility = View.GONE
                        }
                        if (user.usuariosEncontrados.size > 9) {
                            binding.encuentraDiezColor.visibility = View.VISIBLE
                            binding.encuentradiezbyn.visibility = View.GONE
                        }
                        db.collection("Usuarios")
                            .document(Firebase.auth.currentUser!!.email.toString())
                            .collection("Chats").get().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    var contador = 0
                                    it.result?.let {
                                        for (snapshot in it) {
                                            contador++
                                        }
                                    }
                                    if (contador > 0) {
                                        binding.chatunobyn.visibility = View.GONE
                                        binding.chatunocolor.visibility = View.VISIBLE
                                    }
                                    if (contador > 9) {
                                        binding.chatdiezbyn.visibility = View.GONE
                                        binding.chatdiezcolor.visibility = View.VISIBLE
                                    }
                                }
                            }
                    }
                }
            }
    }
}