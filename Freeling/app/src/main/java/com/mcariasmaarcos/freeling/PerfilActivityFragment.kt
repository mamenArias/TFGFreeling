package com.mcariasmaarcos.freeling

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentPerfilActivityBinding

/**
 * Fragment que obtiene los datos del perfil del usuario conectado desde Firebase y los establece en los campos del layout,
 * para poder modificar los que quiera, salvo el emaily y la contraseña.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class PerfilActivityFragment : Fragment(R.layout.fragment_perfil_activity) {
    /** Variable que permite enlazar los elementos del layout **/
    private lateinit var binding:FragmentPerfilActivityBinding
    /** Constante para establecer la conexión a Firebase **/
    private val db = FirebaseFirestore.getInstance()
    /** Variable para establecer la imagen del perfil **/
    private var image: Uri?=null
    /** Variable que sirve para instanciar la clase StorageReference **/
    private lateinit var storageRef: StorageReference
    /** Constante que instancia un objeto de la clase FirebaseStorage **/
    val dbStorage = FirebaseStorage.getInstance()
    /** Constante para los permisos de la galería **/
    private val STORAGEPERMISSIONCODE = 123
    /** Variable que indica el nº de medallas buenas que tiene el usuario **/
    var medallasBuenas:Int = 0
    /** Variable que indica el nº de medallas malas que tiene el usuario **/
    var medallasMalas:Int = 0
    /** Variable que indica si se ha dado una medalla o no a otro usuario **/
    var darMedallas = false
    /** Variable que indica si se ha encontrado o no a otro usuario **/
    var encuentroUno = false
    /** Variable que indica si se ha encontrado o no a otros diez usuarios **/
    var encuentroDiez = false

    /**
     * Función que va a inflar el fragment
     * @param inflater xml del layout que se va a inflar
     * @param container contenedor donde se va a inflar el layout
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = FragmentPerfilActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se van a cargar todos los datos del usuario registrado en los diferentes campos de la pantalla, para poder editar
     * cualquiera de ellos y que se actualicen en Firebase.
     * @param view vista de la actividad.
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Constante con los pronombres que habrá disponibles en el spinner del layout **/
        val pronombres = arrayOf(
            resources.getString(R.string.pronombres),
            resources.getString(R.string.el),
            resources.getString(R.string.ella),
            resources.getString(R.string.elle)
        )

        /** Adapter para pasarle los pronombres al spinner del layout con un estilo determinado **/
        val adapterPron = CustomArrayAdapter(this.requireContext(), R.layout.spinner_editar, pronombres)
        adapterPron.setDropDownViewResource(R.layout.spinner_editar)
        binding.spinnerPronombreRegistroEditar.adapter = adapterPron

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
        val adapterGen = CustomArrayAdapter(this.requireContext(), R.layout.spinner_editar, generos)
        adapterGen.setDropDownViewResource(R.layout.spinner_editar)
        binding.spinnerGeneroRegistroEditar.adapter = adapterGen

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
        val adapterOrient = CustomArrayAdapter(this.requireContext(), R.layout.spinner_editar, orientacion)
        adapterOrient.setDropDownViewResource(R.layout.spinner_editar)
        binding.spinnerOrientacSexRegistroEditar.adapter = adapterOrient

        binding.imagenMedallaPerfil.visibility = View.GONE

        /** Obtención de los datos del usuario conectado para insertarlos en los diferentes campos del layout **/
        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).get()
            .addOnSuccessListener {
                binding.campoEmailRegistroEditar.text = it.get("email").toString()
                Glide.with(this).load(it.get("fotoPerfil")).into(binding.imagenPerfilEditar as ImageView)
                binding.imagenPerfilEditar.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.campoNombreRegistroEditar.setText(it.get("nombre").toString())
                binding.campoEdadRegistroEditar.setText(it.get("edad").toString())
                binding.edadMaxEditar.setText(it.get("edadDeseadaSup").toString())
                binding.edadMinEditar.setText(it.get("edadDeseadaInf").toString())
                binding.campoInteresesRegistroEditar.setText(it.get("biografia").toString())
                medallasBuenas = it.get("medallasBuenas").toString().toInt()
                medallasMalas = it.get("medallasMalas").toString().toInt()
                darMedallas = it.get("darMedallas") as Boolean
                encuentroUno = it.get("encontradoUno") as Boolean
                encuentroDiez = it.get("encontradoDiez") as Boolean
                if (medallasBuenas > medallasMalas){
                    binding.imagenMedallaPerfil.visibility = View.VISIBLE
                    binding.imagenMedallaPerfil.setImageResource(R.drawable.medallabuena)
                } else if (medallasBuenas < medallasMalas) {
                    binding.imagenMedallaPerfil.visibility = View.VISIBLE
                    binding.imagenMedallaPerfil.setImageResource(R.drawable.medallamala)
                }

                for (i in 0 until binding.spinnerPronombreRegistroEditar.count) {
                    if (binding.spinnerPronombreRegistroEditar.getItemAtPosition(i).toString().equals(it.get("pronombre").toString())) {
                        binding.spinnerPronombreRegistroEditar.setSelection(i)
                    }
                }

                for (i in 0 until binding.spinnerGeneroRegistroEditar.count) {
                    if (binding.spinnerGeneroRegistroEditar.getItemAtPosition(i).toString().equals(it.get("genero").toString())) {
                        binding.spinnerGeneroRegistroEditar.setSelection(i)
                    }
                }

                for (i in 0 until binding.spinnerOrientacSexRegistroEditar.count) {
                    if (binding.spinnerOrientacSexRegistroEditar.getItemAtPosition(i).toString().equals(it.get("orientacionSexual").toString())) {
                        binding.spinnerOrientacSexRegistroEditar.setSelection(i)
                    }
                }
            }

        /** Botón que va a modificar los datos del usuario en Firebase, con lo que le estemos pasando a través
         * de los campos **/
        binding.botonModificarPerfil.setOnClickListener {
            if (binding.campoNombreRegistroEditar.text.isNullOrEmpty() || binding.edadMaxEditar.text.isNullOrEmpty() ||
                binding.edadMinEditar.text.isNullOrEmpty() || binding.campoInteresesRegistroEditar.text.isNullOrEmpty() || binding.campoEdadRegistroEditar.text.isNullOrEmpty()) {
                    Toast.makeText(this.context, R.string.camposVacios, Toast.LENGTH_SHORT).show()
            } else if (binding.spinnerPronombreRegistroEditar.selectedItemPosition == 0) {
                Toast.makeText(this.context, R.string.pronombreVacio, Toast.LENGTH_SHORT).show()
            } else if (binding.spinnerGeneroRegistroEditar.selectedItemPosition == 0) {
                Toast.makeText(this.context, R.string.generoVacio, Toast.LENGTH_SHORT).show()
            } else if (binding.spinnerOrientacSexRegistroEditar.selectedItemPosition == 0) {
                Toast.makeText(this.context, R.string.orientacionVacio, Toast.LENGTH_SHORT).show()
            } else if (binding.campoEdadRegistroEditar.text.toString().toByte() < 18) {
                Toast.makeText(this.context, R.string.menorEdad, Toast.LENGTH_SHORT).show()
            } else if (binding.edadMinEditar.text.toString().toByte() >= binding.edadMaxEditar.text.toString().toByte()) {
                Toast.makeText(this.context, R.string.intervaloValido, Toast.LENGTH_SHORT).show()
            } else if (binding.edadMinEditar.text.toString().toByte() < 18) {
                Toast.makeText(this.context, R.string.edadBuscadaValida, Toast.LENGTH_SHORT).show()
            } else if (binding.campoEdadRegistroEditar.text.toString().toInt() > 100 || binding.edadMinEditar.text.toString().toInt() > 100
                || binding.edadMaxEditar.text.toString().toInt() > 100) {
                    Toast.makeText(this.context, R.string.demasiadaEdad, Toast.LENGTH_SHORT).show()
            } else {
               if (image != null) {
                   val reference = FirebaseStorage.getInstance()
                       .getReference("imagenesPerfil/" + binding.campoEmailRegistroEditar.text.toString() + ".jpg")
                   reference.putFile(image!!)
                   storageRef = dbStorage.reference.child("imagenesPerfil")
                       .child(binding.campoEmailRegistroEditar.text.toString() + ".jpg")
               } else {
                   storageRef = dbStorage.reference.child("imagenesPerfil")
                       .child("avatarpordefecto.png")
               }

                storageRef.downloadUrl.addOnSuccessListener { url ->

                    /** Variable usuario con los datos de los campos que luego usaremos para actualizar en Firebase **/
                    var user = Usuario(
                        binding.campoEmailRegistroEditar.text.toString(),
                        binding.campoNombreRegistroEditar.text.toString(),
                        binding.spinnerPronombreRegistroEditar.selectedItem.toString(),
                        binding.spinnerGeneroRegistroEditar.selectedItem.toString(),
                        binding.spinnerOrientacSexRegistroEditar.selectedItem.toString(),
                        binding.campoEdadRegistroEditar.text.toString().toInt(),
                        binding.edadMinEditar.text.toString().toInt(),
                        binding.edadMaxEditar.text.toString().toInt(),
                        binding.campoInteresesRegistroEditar.text.toString(),
                        url.toString(),
                        medallasBuenas,
                        medallasMalas,
                        darMedallas,
                        encuentroUno,
                        encuentroDiez

                    )

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

                    Toast.makeText(this.context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /** Botón para cambiar la foto de perfil del usuario **/
        binding.imagenPerfilEditar.setOnClickListener {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGEPERMISSIONCODE)
        }

        /** Botón para cerrar sesión **/
        binding.botonCerrarSesion.setOnClickListener {
            Firebase.auth.signOut()
            val intent:Intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
    }

    /** Para establecer en el elemento iamgen de perfil, la foto que hemos cogido de la galería del móvil **/
    private val selectImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                image = data!!.data!!
                binding.imagenPerfilEditar.setImageURI(image)
                binding.imagenPerfilEditar.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                Toast.makeText(this.context, "No se ha podido modificar la imagen", Toast.LENGTH_SHORT).show()
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
     * @param requestCode código del permiso pedido
     * @param permissions valor que indica los distintos permisos
     * @param grantResults lista de permisos concedidos.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGEPERMISSIONCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this.context, "Permisos denegados", Toast.LENGTH_SHORT).show()
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
                    view.setTextColor(ContextCompat.getColor(parent.context, R.color.black))
                } else {
                    view.setTextColor(ContextCompat.getColor(parent.context, R.color.white))
                }
            }
            return view
        }
    }
}