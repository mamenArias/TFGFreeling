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
}