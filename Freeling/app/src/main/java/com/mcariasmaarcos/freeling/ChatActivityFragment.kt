package com.mcariasmaarcos.freeling


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.freeling.databinding.FragmentChatActivityBinding
import java.nio.charset.Charset



private lateinit var binding:FragmentChatActivityBinding


class ChatActivityFragment : Fragment(R.layout.fragment_chat_activity) {



override fun onCreateView(
    inflater: LayoutInflater,
    @Nullable container: ViewGroup?,
    @Nullable savedInstanceState: Bundle?
): View? {
    binding = FragmentChatActivityBinding.inflate(inflater, container, false)
    return binding.root
}


}