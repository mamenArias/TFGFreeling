package com.mcariasmaarcos.freeling


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ListView
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


/**
 * The [Message] object used to broadcast information about the device to nearby devices.
 */
private var mMessage: Message? = null

/**
 * A [MessageListener] for processing messages from nearby devices.
 */
private var mMessageListener: MessageListener? = null

/**
 * Adapter for working with messages from nearby publishers.
 */
private var mNearbyDevicesArrayAdapter: ArrayAdapter<String>? = null

private lateinit var binding:FragmentChatActivityBinding

private val db = FirebaseFirestore.getInstance()
/**
 * A simple [Fragment] subclass.
 * Use the [ChatActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatActivityFragment : Fragment(R.layout.fragment_chat_activity) {



override fun onCreateView(
    inflater: LayoutInflater,
    @Nullable container: ViewGroup?,
    @Nullable savedInstanceState: Bundle?
): View? {
    binding = FragmentChatActivityBinding.inflate(inflater, container, false)
    return binding.root
}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var email:String =Firebase.auth.currentUser!!.email.toString()


        mMessage = Message(email.toByteArray(Charset.forName("UTF-8")))

        mMessageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                // Called when a new message is found.
                val msgBody = String(message.content)
                db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).update("usuariosEncontrados",
                    FieldValue.arrayUnion(msgBody)
                )
                mNearbyDevicesArrayAdapter!!.add(msgBody)
            }
            override fun onLost(message: Message) {
                // Called when a message is no longer detectable nearby.
                val msgBody = String(message.content)
                mNearbyDevicesArrayAdapter!!.remove(msgBody)
            }
        }
        val nearbyDevicesArrayList: List<String> = ArrayList()
        mNearbyDevicesArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            nearbyDevicesArrayList
        )
        val nearbyDevicesListView = view.findViewById<View>(R.id.nearby_devices_list_view) as ListView
        if (nearbyDevicesListView != null) {
            nearbyDevicesListView.adapter = mNearbyDevicesArrayAdapter
        }


       binding!!.publishSwitch.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                publish()
                subscribe()
            } else {
                unpublish()
                unsubscribe()
            }
        }

    }

    /**
     * Subscribes to messages from nearby devices and updates the UI if the subscription either
     * fails or TTLs.
     */
    private fun subscribe() {
        Log.i(TAG, "Subscribing")
        mNearbyDevicesArrayAdapter!!.clear()
        val options = SubscribeOptions.Builder()
            .setStrategy(PUB_SUB_STRATEGY)
            .setCallback(object : SubscribeCallback() {
                override fun onExpired() {
                    super.onExpired()
                    Log.i(TAG, "No longer subscribing")
                   // activity!!.runOnUiThread { binding!!.publishSwitch.isChecked = false }
                }
            }).build()
        Nearby.getMessagesClient(requireActivity()).subscribe(mMessageListener!!, options)
    }

    /**
     * Publishes a message to nearby devices and updates the UI if the publication either fails or
     * TTLs.
     */
    private fun publish() {
        Log.i(TAG, "Publishing")
        val options = PublishOptions.Builder()
            .setStrategy(PUB_SUB_STRATEGY)
            .setCallback(object : PublishCallback() {
                override fun onExpired() {
                    super.onExpired()
                    Log.i(TAG, "No longer publishing")
                 //  activity!!.runOnUiThread { binding!!.publishSwitch.isChecked = false }
                }
            }).build()
        Nearby.getMessagesClient(requireActivity()).publish(mMessage!!, options)
          //  .addOnFailureListener { e: Exception? -> logAndShowSnackbar(MISSING_API_KEY) }
    }

    /**
     * Stops subscribing to messages from nearby devices.
     */
    private fun unsubscribe() {
        Log.i(TAG, "Unsubscribing.")
        Nearby.getMessagesClient(requireActivity()).unsubscribe(mMessageListener!!)
    }

    /**
     * Stops publishing message to nearby devices.
     */
    private fun unpublish() {
        Log.i(TAG, "Unpublishing.")
        Nearby.getMessagesClient(requireActivity()).unpublish(mMessage!!)
    }

  /*  private fun logAndShowSnackbar(text: String) {
        Log.w(TAG, text)
        if (binding!!.activityMainContainer != null) {
            Snackbar.make(binding!!.activityMainContainer, text, Snackbar.LENGTH_LONG).show()
        }
    }*/

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val TTL_IN_SECONDS = 3 * 60 // Three minutes.

        /**
         * Sets the time in seconds for a published message or a subscription to live. Set to three
         * minutes in this sample.
         */
        private val PUB_SUB_STRATEGY = Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build()
        private const val MISSING_API_KEY = "It's possible that you haven't added your" +
                " API-KEY. See  " +
                "https://developers.google.com/nearby/messages/android/get-started#step_4_configure_your_project"
    }
}