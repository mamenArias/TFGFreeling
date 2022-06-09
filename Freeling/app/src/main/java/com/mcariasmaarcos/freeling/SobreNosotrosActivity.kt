package com.mcariasmaarcos.freeling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcariasmaarcos.freeling.databinding.ActivityMainBinding
import com.mcariasmaarcos.freeling.databinding.ActivitySobreNosotrosBinding

/**
 * Clase con información de los creadores de la Aplicación e información sobre la misma.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class SobreNosotrosActivity : AppCompatActivity() {
    /** Constante que permite enlazar los elementos del layout **/
    private val binding by lazy { ActivitySobreNosotrosBinding.inflate(layoutInflater) }

    /**
     * Función que cuando se cree el Activity, va a mostrar el contenido.
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}