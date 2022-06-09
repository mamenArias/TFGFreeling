package com.mcariasmaarcos.freeling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.freeling.databinding.ActivityMainBinding

/**
 * Clase inicial de la aplicación, donde el usuario puede iniciar sesión si ya está registrado, introduciendo su email y contraseña,
 * o darle al botón de Registro que le conducirá a otra pantalla en la que darse de alta.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class MainActivity : AppCompatActivity() {
    /** Constante que permite enlazar los elementos del layout **/
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    /**
     * Cuando se crea la actividad, si no hay ningún usuario logueado, vamos a ver la pantalla de inicio de sesión, donde podremos
     * iniciar sesión si tenemos un usuario creado, darle a botón de registro para irnos a otras pantalla o darle al icono de la
     * empresa para que se muestre otra pantalla con información de la app y sus creadores.
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /** Botón para iniciar sesión si el usuario y la contraseña introducidos son correctos **/
        binding.botonInicioSesion.setOnClickListener {
            if (binding.campoUsuario.text.isNullOrEmpty()){
                Toast.makeText(this, R.string.usuarioVacio, Toast.LENGTH_SHORT).show()
            } else if (binding.campoPassword.text.isNullOrEmpty()){
                Toast.makeText(this, R.string.passwVacia, Toast.LENGTH_SHORT).show()
            } else{
                val auth = FirebaseAuth.getInstance()
                val task = auth.signInWithEmailAndPassword(binding.campoUsuario.text.toString(), binding.campoPassword.text.toString())

                task.addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (task.isSuccessful){
                            startActivity(Intent(this@MainActivity, EncuentroActivity::class.java))
                        } else {
                            Toast.makeText(this@MainActivity, R.string.loginInc, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        /** Botón que nos conduce a otra pantalla para realizar el registro de un nuevo usuario dentro de la app **/
        binding.botonRegistro.setOnClickListener {
            val intent = Intent(this@MainActivity, ActivityRegistro::class.java)
            startActivity(intent)
        }

        /** Logo de la empresa que nos va a llevar a una pantalla con los datos de los creadores de la App e información sobre la misma **/
        binding.iconoEquipo.setOnClickListener {
            val intent = Intent(this@MainActivity, SobreNosotrosActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * En esta función se va a comprobobar si hay un usuario logueado llamando a la función [updateUI].
     */
    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        updateUI(currentUser)
    }

    /**
     * Función que comprueba si el usuario ya está logueado, y se salta esta pantalla para acceder a la aplicación directamente,
     * sin tener que volver a loguearse cada vez que abre la app.
     * @param account Usuario registrado en Firebase con el que se accede a la app.
     */
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            val intent = Intent(this@MainActivity, EncuentroActivity::class.java)
            intent.putExtra("usuario", account.email)
            startActivity(intent)
        }
    }
}