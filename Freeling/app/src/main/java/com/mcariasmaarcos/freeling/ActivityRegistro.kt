package com.mcariasmaarcos.freeling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.mcariasmaarcos.freeling.databinding.ActivityRegistroBinding

class ActivityRegistro : AppCompatActivity() {

    val binding by lazy { ActivityRegistroBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pronombres = arrayOf("Él", "Ella", "Elle")
        val adapter = ArrayAdapter(this, R.layout.spinner_estilo, pronombres)
        adapter.setDropDownViewResource(R.layout.spinner_estilo)
        binding.spinnerPronombreRegistro.adapter = adapter
    }
}