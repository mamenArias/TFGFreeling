package com.mcariasmaarcos.freeling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcariasmaarcos.freeling.databinding.ActivityChatMensajesBinding
import com.mcariasmaarcos.freeling.databinding.ActivityMainBinding

class ChatMensajesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatMensajesBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}