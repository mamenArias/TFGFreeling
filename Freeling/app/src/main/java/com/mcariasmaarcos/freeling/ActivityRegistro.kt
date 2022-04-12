package com.mcariasmaarcos.freeling

import android.content.Intent
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.mcariasmaarcos.freeling.databinding.ActivityRegistroBinding
import java.util.jar.Manifest

class ActivityRegistro : AppCompatActivity() {

    val binding by lazy { ActivityRegistroBinding.inflate(layoutInflater) }
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageRef: StorageReference

    private val STORAGE_PERMISSION_CODE = 123
    private lateinit var image:Uri

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





        binding.imagenPerfil.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
        }
    }


}

    private val selectImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                image = data!!.data!!
                binding.imagenPerfil.setImageURI(image)
            } else {
                Toast.makeText(this, "No se ha podido modificar la imagen", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        selectImageResultLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage()
            }else{
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }
