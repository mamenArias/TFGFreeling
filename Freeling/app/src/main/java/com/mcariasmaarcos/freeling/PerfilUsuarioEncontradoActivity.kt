package com.mcariasmaarcos.freeling

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.freeling.databinding.ActivityPerfilUsuarioEncontradoBinding

class PerfilUsuarioEncontradoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPerfilUsuarioEncontradoBinding.inflate(layoutInflater) }
    private val db = FirebaseFirestore.getInstance()
    private var image: Uri?=null
    private var otroUsuario:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra("otroUsuario")?.let { otroUsuario = it }

        db.collection("Usuarios").document(otroUsuario).get()
            .addOnSuccessListener{
                binding.emailOtro.text = it.get("email").toString()
                Glide.with(this).load(it.get("fotoPerfil"))
                    .into(binding.imagenPerfilOtro as ImageView)
                binding.imagenPerfilOtro.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.nombreOtro.setText(it.get("nombre").toString())
                binding.pronombreOtro.setText(it.get("pronombre").toString())
                binding.generoOtro.setText(it.get("genero").toString())
                binding.orientacionSexOtro.setText(it.get("orientacionSexual").toString())
                binding.edadOtro.setText(it.get("edad").toString())
                binding.edadMaxOtro.setText(it.get("edadDeseadaSup").toString())
                binding.edadMinOtro.setText(it.get("edadDeseadaInf").toString())
                binding.interesesOtro.setText(it.get("biografia").toString())
            }
    }
}