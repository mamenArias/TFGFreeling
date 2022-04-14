package com.mcariasmaarcos.freeling

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentPerfilActivityBinding


/**
 * A simple [Fragment] subclass.
 * Use the [PerfilActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilActivityFragment : Fragment(R.layout.fragment_perfil_activity) {
    private lateinit var binding:FragmentPerfilActivityBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private var image: Uri?=null
    /** Variable que sirve para instanciar la clase StorageReference**/
    private lateinit var storageRef: StorageReference

    /** Constante que instancia un objeto de la clase FirebaseStorage**/
    val dbStorage = FirebaseStorage.getInstance()
    private val STORAGEPERMISSIONCODE = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pronombres = arrayOf(
            resources.getString(R.string.pronombres),
            resources.getString(R.string.el),
            resources.getString(R.string.ella),
            resources.getString(R.string.elle)
        )
        val adapterPron =
            EditarPerfilActivity.CustomArrayAdapter(this.context, R.layout.spinner_editar, pronombres)
        adapterPron.setDropDownViewResource(R.layout.spinner_editar)
        binding.spinnerPronombreRegistroEditar.adapter = adapterPron

        val generos = arrayOf(
            resources.getString(R.string.genero),
            resources.getString(R.string.hombre),
            resources.getString(R.string.mujer),
            resources.getString(R.string.hombreTrans),
            resources.getString(R.string.mujerTrans),
            resources.getString(R.string.androgino),
            resources.getString(R.string.neutro),
            resources.getString(R.string.queer),
            resources.getString(R.string.noBinario),
            resources.getString(R.string.agenero),
            resources.getString(R.string.bigenero),
            resources.getString(R.string.fluido),
            resources.getString(R.string.poligenero),
            resources.getString(R.string.intergenero)
        )
        val adapterGen =
            EditarPerfilActivity.CustomArrayAdapter(this.context, R.layout.spinner_editar, generos)
        adapterGen.setDropDownViewResource(R.layout.spinner_editar)
        binding.spinnerGeneroRegistroEditar.adapter = adapterGen

        val orientacion = arrayOf(
            resources.getString(R.string.orientacion),
            resources.getString(R.string.hetero),
            resources.getString(R.string.homo),
            resources.getString(R.string.bisexual),
            resources.getString(R.string.pansexual),
            resources.getString(R.string.asexual),
            resources.getString(R.string.demisexual),
            resources.getString(R.string.lightsexual),
            resources.getString(R.string.autosexual),
            resources.getString(R.string.antrosexual),
            resources.getString(R.string.polisexual)
        )
        val adapterOrient =
            EditarPerfilActivity.CustomArrayAdapter(this.context, R.layout.spinner_editar, orientacion)
        adapterOrient.setDropDownViewResource(R.layout.spinner_editar)
        binding.spinnerOrientacSexRegistroEditar.adapter = adapterOrient



        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get()
            .addOnSuccessListener {
                binding.campoEmailRegistroEditar.text = it.get("email").toString()
                Glide.with(this).load(it.get("fotoPerfil"))
                    .into(binding.imagenPerfilEditar as ImageView)
                binding.imagenPerfilEditar.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.campoNombreRegistroEditar.setText(it.get("nombre").toString())
                binding.campoEdadRegistroEditar.setText(it.get("edad").toString())
                binding.edadMaxEditar.setText(it.get("edadDeseadaSup").toString())
                binding.edadMinEditar.setText(it.get("edadDeseadaInf").toString())
                binding.campoInteresesRegistroEditar.setText(it.get("biografia").toString())

                for (i in 0 until binding.spinnerPronombreRegistroEditar.count) {

                    if (binding.spinnerPronombreRegistroEditar.getItemAtPosition(i).toString()
                            .equals(it.get("pronombre").toString())
                    ) {
                        binding.spinnerPronombreRegistroEditar.setSelection(i)
                    }

                }

                for (i in 0 until binding.spinnerGeneroRegistroEditar.count) {

                    if (binding.spinnerGeneroRegistroEditar.getItemAtPosition(i).toString()
                            .equals(it.get("genero").toString())
                    ) {
                        binding.spinnerGeneroRegistroEditar.setSelection(i)
                    }

                }

                for (i in 0 until binding.spinnerOrientacSexRegistroEditar.count) {

                    if (binding.spinnerOrientacSexRegistroEditar.getItemAtPosition(i).toString()
                            .equals(it.get("orientacionSexual").toString())
                    ) {
                        binding.spinnerOrientacSexRegistroEditar.setSelection(i)
                    }

                }


            }


        binding.botonModificarPerfil.setOnClickListener {
            if (binding.campoNombreRegistroEditar.text.isNullOrEmpty() || binding.edadMaxEditar.text.isNullOrEmpty() ||
                binding.edadMinEditar.text.isNullOrEmpty() || binding.campoInteresesRegistroEditar.text.isNullOrEmpty()||
                binding.campoEdadRegistroEditar.text.isNullOrEmpty()
            ) {
                Toast.makeText(this.context, R.string.camposVacios, Toast.LENGTH_SHORT).show()
            } else if (binding.spinnerPronombreRegistroEditar.selectedItemPosition == 0) {
                Toast.makeText(this.context, R.string.pronombreVacio, Toast.LENGTH_SHORT).show()
            } else if (binding.spinnerGeneroRegistroEditar.selectedItemPosition == 0) {
                Toast.makeText(this.context, R.string.generoVacio, Toast.LENGTH_SHORT).show()
            } else if (binding.spinnerOrientacSexRegistroEditar.selectedItemPosition == 0) {
                Toast.makeText(this.context, R.string.orientacionVacio, Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.campoEdadRegistroEditar.text.toString().toByte() < 18) {
                Toast.makeText(this.context, R.string.menorEdad, Toast.LENGTH_SHORT).show()
            } else if (binding.edadMinEditar.text.toString()
                    .toByte() >= binding.edadMaxEditar.text.toString().toByte()
            ) {
                Toast.makeText(this.context, R.string.intervaloValido, Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.edadMinEditar.text.toString().toByte() < 18) {
                Toast.makeText(this.context, R.string.edadBuscadaValida, Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.campoEdadRegistroEditar.text.toString()
                    .toInt() > 100 || binding.edadMinEditar.text.toString().toInt() > 100
                || binding.edadMaxEditar.text.toString().toInt() > 100
            ) {
                Toast.makeText(this.context, R.string.demasiadaEdad, Toast.LENGTH_SHORT).show()
            } else {

               if (image != null) {

                    val reference = FirebaseStorage.getInstance()
                        .getReference("imagenesPerfil/" + binding.campoEmailRegistroEditar.text.toString() + ".jpg")
                    reference.putFile(image!!)
                    storageRef =
                        dbStorage.reference.child("imagenesPerfil")
                            .child(binding.campoEmailRegistroEditar.text.toString() + ".jpg")

                } else {
                    storageRef =
                        dbStorage.reference.child("imagenesPerfil")
                            .child("avatarpordefecto.png")
                }
                storageRef.downloadUrl.addOnSuccessListener { url ->



                    var user = Usuario(
                        binding.campoEmailRegistroEditar.text.toString(),
                        binding.campoNombreRegistroEditar.text.toString(),
                        binding.spinnerPronombreRegistroEditar.selectedItem.toString(),
                        binding.spinnerGeneroRegistroEditar.selectedItem.toString(),
                        binding.spinnerOrientacSexRegistroEditar.selectedItem.toString(),
                        binding.campoEdadRegistroEditar.text.toString()
                            .toInt(),
                        binding.edadMinEditar.text.toString().toInt(),
                        binding.edadMaxEditar.text.toString().toInt(),
                        binding.campoInteresesRegistroEditar.text.toString(),
                        url.toString()
                    )

                    //db.collection("Usuarios").document(user.email).delete()

                    //db.collection("Usuarios").document(user.email).set(user)
                    db.collection("Usuarios").document(user.email).update(
                        "email",user.email,
                        "nombre",user.nombre,
                        "pronombre",user.pronombre,
                        "genero",user.genero,
                        "edadDeseadaSup",user.edadDeseadaSup,
                        "edadDeseadaInf",user.edadDeseadaInf,
                        "orientacionSexual",user.orientacionSexual,
                        "edad",user.edad,
                        "biografia",user.biografia,
                        "fotoPerfil",user.fotoPerfil

                    )

                    Toast.makeText(
                        this.context,
                        "Perfil actualizado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        binding.imagenPerfilEditar.setOnClickListener {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGEPERMISSIONCODE
            )
        }
    }






    private val selectImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                image = data!!.data!!
                binding.imagenPerfilEditar.setImageURI(image)
                binding.imagenPerfilEditar.scaleType = ImageView.ScaleType.CENTER_CROP

            } else {
                Toast.makeText(this.context, "No se ha podido modificar la imagen", Toast.LENGTH_SHORT)
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
        if (requestCode == STORAGEPERMISSIONCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this.context, "Permisos denegados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class CustomArrayAdapter(context: Context, resource: Int, objects: Array<String>) : ArrayAdapter<String>(context, resource, objects) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            if (view is TextView) {
                if (position == 0) {
                    view.setTextColor(ContextCompat.getColor(parent.context, R.color.black))
                } else {
                    view.setTextColor(ContextCompat.getColor(parent.context, R.color.white))
                }
            }
            return view
        }
    }
}