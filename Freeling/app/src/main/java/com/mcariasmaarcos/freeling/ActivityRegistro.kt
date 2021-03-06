package com.mcariasmaarcos.freeling

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.ActivityRegistroBinding

/**
 * Clase para que el usuario se registre por primera vez en la aplicación. Se cogerán los datos introducidos en los campos
 * correspondientes, teniendo en cuenta que ninguno puede quedar vacío, y se añadirán estos datos a Firebase.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class ActivityRegistro : AppCompatActivity() {
    /** Constante que permite enlazar los elementos del layout **/
    val binding by lazy { ActivityRegistroBinding.inflate(layoutInflater) }
    /** Variable que sirve para instanciar la clase StorageReference **/
    private lateinit var storageRef: StorageReference
    /** Constante que instancia un objeto de la clase FirebaseStorage **/
    val dbStorage = FirebaseStorage.getInstance()
    /** Constante para establecer la conexión a Firebase **/
    private val db = FirebaseFirestore.getInstance()
    /** Constante para los permisos de la galería **/
    private val STORAGEPERMISSIONCODE = 123
    /** Variable para establecer la imagen del perfil **/
    private var image: Uri? = null

    /**
     * Función que cuando se cree la actividad, va a cargar todos los campos que el usuario debe rellenar con sus datos
     * para el correcto registro del mismo en la base de datos.
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /** Constante con los pronombres que habrá disponibles en el spinner del layout **/
        val pronombres = arrayOf(
            resources.getString(R.string.pronombres),
            resources.getString(R.string.el),
            resources.getString(R.string.ella),
            resources.getString(R.string.elle)
        )

        /** Adapter para pasarle los pronombres al spinner del layout con un estilo determinado **/
        val adapterPron = CustomArrayAdapter(this, R.layout.spinner_estilo, pronombres)
        adapterPron.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerPronombreRegistro.adapter = adapterPron

        /** Constante con los géneros que habrá disponibles en el spinner del layout **/
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

        /** Adapter para pasarle los géneros al spinner del layout con un estilo determinado **/
        val adapterGen = CustomArrayAdapter(this, R.layout.spinner_estilo, generos)
        adapterGen.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerGeneroRegistro.adapter = adapterGen

        /** Constante con las orientaciones sexuales que habrá disponibles en el spinner del layout **/
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

        /** Adapter para pasarle las orientaciones sexuales al spinner del layout con un estilo determinado **/
        val adapterOrient = CustomArrayAdapter(this, R.layout.spinner_estilo, orientacion)
        adapterOrient.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerOrientacSexRegistro.adapter = adapterOrient

        /** Botón para registrar al usuario y añadir sus datos a Firebase **/
        binding.botonRegistrarUsuario.setOnClickListener {
            val regex = Regex(pattern = ".*[0-9].*")
            if (binding.campoUsuarioRegistro.text.isNullOrEmpty() || binding.campoPasswRegistro.text.isNullOrEmpty()
                || binding.campoNombreRegistro.text.isNullOrEmpty() || binding.edadMax.text.isNullOrEmpty()
                || binding.edadMin.text.isNullOrEmpty() || binding.campoInteresesRegistro.text.isNullOrEmpty()
                || binding.campoEdadRegistro.text.isNullOrEmpty()) {
                    Toast.makeText(this, R.string.camposVacios, Toast.LENGTH_SHORT).show()
            } else {
                val userDocRef = FirebaseFirestore.getInstance().collection("Usuarios").document(binding.campoUsuarioRegistro.text.toString())
                userDocRef.get().addOnCompleteListener() { task ->
                    val result = task.result
                    if (result.exists()) {
                        Toast.makeText(this@ActivityRegistro,R.string.emailExistente,Toast.LENGTH_SHORT).show()
                    } else {
                        if (binding.spinnerPronombreRegistro.selectedItemPosition == 0) {
                            Toast.makeText(this, R.string.pronombreVacio, Toast.LENGTH_SHORT).show()
                        } else if (binding.spinnerGeneroRegistro.selectedItemPosition == 0) {
                            Toast.makeText(this, R.string.generoVacio, Toast.LENGTH_SHORT).show()
                        } else if (binding.spinnerOrientacSexRegistro.selectedItemPosition == 0) {
                            Toast.makeText(this, R.string.orientacionVacio, Toast.LENGTH_SHORT).show()
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.campoUsuarioRegistro.text).matches()) {
                            Toast.makeText(this, R.string.emailValido, Toast.LENGTH_SHORT).show()
                        } else if (binding.campoPasswRegistro.text.toString().length < 8) {
                            Toast.makeText(this, R.string.contraseniaLongitud, Toast.LENGTH_SHORT).show()
                        } else if (!regex.containsMatchIn(input = binding.campoPasswRegistro.text.toString())) {
                            Toast.makeText(this, R.string.contraseniaInCarac, Toast.LENGTH_LONG).show()
                        } else if (binding.campoEdadRegistro.text.toString().toByte() < 18) {
                            Toast.makeText(this, R.string.menorEdad, Toast.LENGTH_SHORT).show()
                        } else if (binding.edadMin.text.toString().toByte() >= binding.edadMax.text.toString().toByte()) {
                            Toast.makeText(this, R.string.intervaloValido, Toast.LENGTH_SHORT).show()
                        } else if (binding.edadMin.text.toString().toByte() < 18) {
                            Toast.makeText(this, R.string.edadBuscadaValida, Toast.LENGTH_SHORT).show()
                        } else if (binding.campoEdadRegistro.text.toString().toInt() > 100 || binding.edadMin.text.toString().toInt() > 100 || binding.edadMax.text.toString().toInt() > 100) {
                            Toast.makeText(this, R.string.demasiadaEdad, Toast.LENGTH_SHORT).show()
                        } else {
                            val auth = FirebaseAuth.getInstance()
                            val tarea = auth.createUserWithEmailAndPassword(
                                binding.campoUsuarioRegistro.text.toString(),
                                binding.campoPasswRegistro.text.toString()
                            )

                            tarea.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                                override fun onComplete(p0: Task<AuthResult>) {
                                    if (tarea.isSuccessful) {
                                        Toast.makeText(this@ActivityRegistro,"Éxito",Toast.LENGTH_SHORT).show()

                                        if (image != null) {
                                            val reference = FirebaseStorage.getInstance()
                                                .getReference("imagenesPerfil/" + binding.campoUsuarioRegistro.text.toString() + ".jpg")
                                            reference.putFile(image!!)
                                            storageRef = dbStorage.reference.child("imagenesPerfil")
                                                .child(binding.campoUsuarioRegistro.text.toString() + ".jpg")
                                        } else {
                                            storageRef = dbStorage.reference.child("imagenesPerfil")
                                                .child("avatarpordefecto.png")
                                        }

                                        storageRef.downloadUrl.addOnSuccessListener { url ->

                                            /** Variable usuario con los datos de los campos que luego usaremos para actualizar en Firebase **/
                                            var user = Usuario(
                                                binding.campoUsuarioRegistro.text.toString(),
                                                binding.campoNombreRegistro.text.toString(),
                                                binding.spinnerPronombreRegistro.selectedItem.toString(),
                                                binding.spinnerGeneroRegistro.selectedItem.toString(),
                                                binding.spinnerOrientacSexRegistro.selectedItem.toString(),
                                                binding.campoEdadRegistro.text.toString().toInt(),
                                                binding.edadMin.text.toString().toInt(),
                                                binding.edadMax.text.toString().toInt(),
                                                binding.campoInteresesRegistro.text.toString(),
                                                url.toString(),
                                                0,
                                                0,
                                                false,
                                                false,
                                                false,
                                                arrayListOf<String>()
                                            )

                                            db.collection("Usuarios").document(user.email).set(user)
                                            startActivity(Intent(this@ActivityRegistro, MainActivity::class.java))
                                        }

                                    } else {
                                        Toast.makeText(this@ActivityRegistro, "Fallo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }

        /** Botón para cambiar la foto de perfil del usuario **/
        binding.imagenPerfil.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGEPERMISSIONCODE)
        }
    }

    /** Para establecer en el elemento iamgen de perfil, la foto que hemos cogido de la galería del móvil **/
    private val selectImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                image = data!!.data!!
                binding.imagenPerfil.setImageURI(image)
                binding.imagenPerfil.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                Toast.makeText(this, "No se ha podido modificar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    )

    /**
     * Función para coger una foto de la galería del móvil
     */
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        selectImageResultLauncher.launch(intent)
    }

    /**
     * Función para solicitar permisos al usuario para acceder a la galería a la hora de modificar la foto de perfil
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGEPERMISSIONCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Clase que va a aplicar un estilo determinado a los spinner utilizados en el layout
     * @param context Contexto en el que se aplica
     * @param resource Layout aplicado como estilo
     * @param objects Array de String que se verá en el spinner
     */
    class CustomArrayAdapter(context: Context, resource: Int, objects: Array<String>) : ArrayAdapter<String>(context, resource, objects) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            if (view is TextView) {
                if (position == 0) {
                    view.setTextColor(ContextCompat.getColor(parent.context, R.color.gris))
                } else {
                    view.setTextColor(ContextCompat.getColor(parent.context, R.color.white))
                }
            }
            return view
        }
    }
}
