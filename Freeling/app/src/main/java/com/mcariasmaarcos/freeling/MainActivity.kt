package com.mcariasmaarcos.freeling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.freeling.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                            startActivity(Intent(this@MainActivity, EditarPerfilActivity::class.java))
                        } else {
                            Toast.makeText(this@MainActivity, R.string.loginInc, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        binding.botonRegistro.setOnClickListener {
            val intent:Intent = Intent(this@MainActivity, ActivityRegistro::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        // comprueba si hay usuario logueado
        val currentUser = Firebase.auth.currentUser
        updateUI(currentUser)


    }

    //si el usuario esta dentro ya se salta el login
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            startActivity(Intent(this@MainActivity, EditarPerfilActivity::class.java))
        } else {

        }
    }

}