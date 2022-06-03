package com.mcariasmaarcos.freeling

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.mcariasmaarcos.freeling.databinding.ActivityPerfilUsuarioEncontradoBinding

/**
 * Clase que nos muestra los datos del usuario que se ha almacenado en nuestra lista de usuarios encontrados una vez que nos lo
 * hemos cruzado por la calle. Accedemos a ella una vez que hacemos click sobre dicho usuario en la lista.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class PerfilUsuarioEncontradoActivity : AppCompatActivity() {
    /** Constante que permite enlazar los elementos del layout **/
    private val binding by lazy { ActivityPerfilUsuarioEncontradoBinding.inflate(layoutInflater) }
    /** Constante para establecer la conexión a Firebase **/
    private val db = FirebaseFirestore.getInstance()
    /** Email del usuario encontrado al que queremos acceder para ver su perfil **/
    private var otroUsuario:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra("otroUsuario")?.let { otroUsuario = it }

        binding.imagenMedalla.visibility = View.GONE

        db.collection("Usuarios").document(otroUsuario).get()
            .addOnSuccessListener{
                binding.emailOtro.text = it.get("email").toString()
                Glide.with(this).load(it.get("fotoPerfil")).into(binding.imagenPerfilOtro as ImageView)
                binding.imagenPerfilOtro.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.nombreOtro.setText(it.get("nombre").toString())
                binding.pronombreOtro.setText(it.get("pronombre").toString())
                binding.generoOtro.setText(it.get("genero").toString())
                binding.orientacionSexOtro.setText(it.get("orientacionSexual").toString())
                binding.edadOtro.setText(it.get("edad").toString())
                binding.edadMaxOtro.setText(it.get("edadDeseadaSup").toString())
                binding.edadMinOtro.setText(it.get("edadDeseadaInf").toString())
                binding.interesesOtro.setText(it.get("biografia").toString())
                if (it.get("medallasBuenas").toString().toInt() > it.get("medallasMalas").toString().toInt()){
                    binding.imagenMedalla.visibility = View.VISIBLE
                    binding.imagenMedalla.setImageResource(R.drawable.medallabuena)
                } else if (it.get("medallasBuenas").toString().toInt() < it.get("medallasMalas").toString().toInt()) {
                    binding.imagenMedalla.visibility = View.VISIBLE
                    binding.imagenMedalla.setImageResource(R.drawable.medallamala)
                }
            }
    }
}