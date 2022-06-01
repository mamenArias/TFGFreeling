package com.mcariasmaarcos.freeling


import android.net.Uri
import android.opengl.Visibility
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
import com.mcariasmaarcos.freeling.databinding.FragmentEncuentroActivityBinding
import com.mcariasmaarcos.freeling.databinding.FragmentLogrosActivityBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LogrosActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogrosActivityFragment : Fragment(R.layout.fragment_logros_activity) {

    private lateinit var binding: FragmentLogrosActivityBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    val dbStorage = FirebaseStorage.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
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
//                    adapter.notifyDataSetChanged() //le avisamos al adapter que tiene nuevos elementos
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