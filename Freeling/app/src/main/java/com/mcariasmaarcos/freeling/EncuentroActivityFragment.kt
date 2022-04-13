package com.mcariasmaarcos.freeling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentEncuentroActivityBinding
import com.mcariasmaarcos.recycler.RecyclerUsuariosEncontradosAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [EncuentroActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EncuentroActivityFragment : Fragment(R.layout.fragment_encuentro_activity) {

    private lateinit var binding:FragmentEncuentroActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEncuentroActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listaUsuarios:ArrayList<String> = arrayListOf<String>()
        listaUsuarios.add("moneite@gmail.com")
        listaUsuarios.add("mamen@gmail.com")

        val adapter:RecyclerUsuariosEncontradosAdapter = RecyclerUsuariosEncontradosAdapter(this.context,listaUsuarios)
        binding.recyclerPersonasEncontradas.adapter = adapter
        binding.recyclerPersonasEncontradas.layoutManager = LinearLayoutManager(this.context)
    }




}