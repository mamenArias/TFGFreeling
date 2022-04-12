package com.mcariasmaarcos.freeling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcariasmaarcos.freeling.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.botonRegistro.setOnClickListener {
            val intent:Intent = Intent(this@MainActivity, ActivityRegistro::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        /* comprueba si hay usuario logueado
        val currentUser = auth.currentUser
        updateUI(currentUser)
         */

    }

    /* si el usuario esta dentro ya se salta el login
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            startActivity(Intent(this,PantallaPrincipalconelchat::java.class))
        } else {

        }
    }
     */
}