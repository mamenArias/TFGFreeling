package com.mcariasmaarcos.freeling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mcariasmaarcos.freeling.databinding.ActivityEncuentroBinding

/**
 * Clase que va a controlar el acceso a los diferentes fragments creados, a través del bottom menu de la pantalla.
 */
class EncuentroActivity : AppCompatActivity() {

    val binding by lazy { ActivityEncuentroBinding.inflate(layoutInflater)}
    //private var usuario = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra("usuario")?.let { user = it }

        val homeFragment = EncuentroActivityFragment()
        val chatFragment = ChatActivityFragment()
        val logrosFragment = LogrosActivityFragment()
        val perfilFragment = PerfilActivityFragment()

        seleccionarFragment(homeFragment)

        binding.menuInferior.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->seleccionarFragment(homeFragment)
                R.id.chat->seleccionarFragment(chatFragment)
                R.id.logros->seleccionarFragment(logrosFragment)
                R.id.perfil->seleccionarFragment(perfilFragment)
            }
            true
        }
    }

    /**
     * Función que va a sustituir un fragment por otro en el layout según el botón del menú inferior que se pulse.
     */
    private fun seleccionarFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.espacioFragments, fragment)
            commit()
        }
    }
}