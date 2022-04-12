package com.mcariasmaarcos.freeling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.mcariasmaarcos.freeling.databinding.ActivityRegistroBinding

class ActivityRegistro : AppCompatActivity() {

    val binding by lazy { ActivityRegistroBinding.inflate(layoutInflater) }
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pronombres = arrayOf(resources.getString(R.string.el), resources.getString(R.string.ella),
        resources.getString(R.string.elle))
        val adapterPron = ArrayAdapter(this, R.layout.spinner_estilo, pronombres)
        adapterPron.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerPronombreRegistro.adapter = adapterPron

        val generos = arrayOf(resources.getString(R.string.hombre), resources.getString(R.string.mujer),
            resources.getString(R.string.hombreTrans), resources.getString(R.string.mujerTrans),
            resources.getString(R.string.androgino), resources.getString(R.string.neutro),
            resources.getString(R.string.queer), resources.getString(R.string.noBinario),
            resources.getString(R.string.agenero), resources.getString(R.string.bigenero),
            resources.getString(R.string.fluido), resources.getString(R.string.poligenero), resources.getString(R.string.intergenero))
        val adapterGen = ArrayAdapter(this, R.layout.spinner_estilo, generos)
        adapterGen.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerGeneroRegistro.adapter = adapterGen

        val orientacion = arrayOf(resources.getString(R.string.hetero), resources.getString(R.string.homo),
            resources.getString(R.string.bisexual), resources.getString(R.string.pansexual), resources.getString(R.string.asexual),
            resources.getString(R.string.demisexual), resources.getString(R.string.lightsexual),
            resources.getString(R.string.autosexual), resources.getString(R.string.antrosexual), resources.getString(R.string.polisexual))
        val adapterOrient = ArrayAdapter(this, R.layout.spinner_estilo, orientacion)
        adapterOrient.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerOrientacSexRegistro.adapter = adapterOrient

        binding.botonRegistrarUsuario.setOnClickListener {

        if(binding.campoUsuarioRegistro.text.isEmpty()||binding.campoPasswRegistro.text.isEmpty()
            ||binding.campoNombreRegistro.text.isEmpty()){
            //TODO completar los empty y eso para hacer todas las comprobaciones necesarias
        }else{
            val auth=FirebaseAuth.getInstance()
            val tarea =auth.createUserWithEmailAndPassword(
                binding.campoUsuarioRegistro.text.toString(),binding.campoPasswRegistro.text.toString()
            )

            tarea.addOnCompleteListener (this, object:OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {
                    if(tarea.isSuccessful){
                        Toast.makeText(this@ActivityRegistro, "Éxito", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@ActivityRegistro, "Fallo", Toast.LENGTH_SHORT).show()
                    }
                }


            })


        }





        }
    }

    override fun onStart() {
        super.onStart()

    }



}