package com.mcariasmaarcos.freeling

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mcariasmaarcos.clases.Usuario
import com.mcariasmaarcos.freeling.databinding.FragmentEncuentroActivityBinding
import com.mcariasmaarcos.recycler.RecyclerUsuariosEncontradosAdapter
import java.nio.charset.Charset

/**
 * Fragment que va a almacenar todos los usuarios que el propietario de la App se encuentre por la calle a través de Google Nearby.
 * Se añadirán en un recycler y desde ahí se podrán agregar a otra lista para poder chatear con ellos, o eliminarlos directamente
 * si no está interesado.
 * @author Miguel Ángel Arcos Reyes
 * @author Mª Carmen Arias de Haro
 * @since 1.2
 */
class EncuentroActivityFragment : Fragment(R.layout.fragment_encuentro_activity) {

    /** Variable que permite enlazar los elementos del layout **/
    private lateinit var binding: FragmentEncuentroActivityBinding
    /** Constante para establecer la conexión a Firebase **/
    private val db = FirebaseFirestore.getInstance()

    /** El objeto [Mensaje] utilizado para transmitir información sobre el dispositivo a los dispositivos cercanos **/
    private var mMessage: Message? = null

    /** Un [MessageListener] para procesar los mensajes de los dispositivos cercanos **/
    private var mMessageListener: MessageListener? = null

    /** Adapter para trabajar con mensajes de editores cercanos **/
    private var adapter: RecyclerUsuariosEncontradosAdapter? = null

    /** ArrayList de usuarios que serán los que se vayan encontrando a través del Google N**/
    var listaUsuarios: ArrayList<String> = arrayListOf<String>()

    /**
     * Función que va a inflar el fragment
     * @param inflater xml del layout que se va a inflar????
     * @param container contenedor donde se va a inflar el layout
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = FragmentEncuentroActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Función en la que se va a cargar el recycler con los diferentes usuarios que nos encontramos gracias a Google Nearby,
     * teniendo en cuenta las condiciones de edad mínima y máxima que haya establecido el usuario.
     * @param view vista de la actividad.
     * @param savedInstanceState objeto Bundle que contiene el estado ya guardado de la actividad.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Variable a la que se pasarán los datos del usuario obtenidos de Firebase **/
        lateinit var user: Usuario
        /** Email del usuario logueado **/
        lateinit var email:String
        /** Edad mínima que busca el usuario logueado **/
        var edadInf = -1
        /** Edad máxima que busca el usuario logueado **/
        var edadSup = -1

        refreshFragment()

        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString())
            .get() //Al debuguear, se detiene en esta linea y salta hasta el return, dando como resultado un 0 cuando se recibe en el adaptador.
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user = it.result.toObject(Usuario::class.java)!!
//                    adapter.notifyDataSetChanged() //le avisamos al adapter que tiene nuevos elementos
                    if (user != null) {
                        //Toast.makeText(this.context, "llegue", Toast.LENGTH_SHORT).show()
                        listaUsuarios = user!!.usuariosEncontrados
                        adapter =
                            RecyclerUsuariosEncontradosAdapter(this.context, listaUsuarios)
                        binding.recyclerPersonasEncontradas.adapter = adapter
                        binding.recyclerPersonasEncontradas.layoutManager =
                            LinearLayoutManager(this.context)
                            binding.publishSwitch.visibility = View.VISIBLE
                            binding.lblCargando.visibility = View.GONE
                        edadSup = it.result.get("edadDeseadaSup").toString().toInt()
                        edadInf = it.result.get("edadDeseadaInf").toString().toInt()
                        // RELLENAR VARIABLES INTERNAS DE EMAIL Y EL MMESSAGE
                        //PONER MENSAJE DE CARGA y poner el BINDING DEL SWITCH
                    // ACTIVAR EL SWITCH
                    }
                }
         }
//            .addOnSuccessListener {
//                if (user != null) {
//                    //Toast.makeText(this.context, "llegue", Toast.LENGTH_SHORT).show()
//                    listaUsuarios = user!!.usuariosEncontrados
//                    adapter =
//                        RecyclerUsuariosEncontradosAdapter(this.context, listaUsuarios)
//                    binding.recyclerPersonasEncontradas.adapter = adapter
//                    binding.recyclerPersonasEncontradas.layoutManager =
//                        LinearLayoutManager(this.context)
//                }
//            }

        email =Firebase.auth.currentUser!!.email.toString()
        mMessage = Message(email.toByteArray(Charset.forName("UTF-8")))

        mMessageListener = object : MessageListener() {
            /**
             * Función que recibe un mensaje del Listener
             * Una vez encuentra un mensaje, es decir, email del usuario encontrado comprueba su edad y si está
             * dentro de los límites de edad que el usuario busca se añade a la lista de usuarios encontrados
             * @param message mensaje que encuentra el listener
             */
            override fun onFound(message: Message) {
                val msgBody = String(message.content)
                //Me traigo el usuario encontrado cojo su edad y compruebo.
                db.collection("Usuarios").document(msgBody).get().addOnSuccessListener {
                   var edadEncontrada = it.get("edad").toString().toInt()
                    if(edadEncontrada >= edadInf && edadEncontrada <= edadSup ){
                        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).update("usuariosEncontrados",
                            FieldValue.arrayUnion(msgBody)
                        )
                        listaUsuarios = user!!.usuariosEncontrados
                        adapter!!.setData(listaUsuarios)
                    }
                }
                refreshFragment()
//                requireActivity().getSupportFragmentManager().findFragmentById(R.id.home)?.let {
//                    activity!!.getSupportFragmentManager()
//                        .beginTransaction()
//                        .detach(it) // detach the current fragment
//                       .attach(requireParentFragment()) // attach with current fragment
//                        .commit()
//                };
//                val adapter: RecyclerUsuariosEncontradosAdapter =
//                    RecyclerUsuariosEncontradosAdapter(requireActivity().getApplicationContext(), listaUsuarios)
//                binding.recyclerPersonasEncontradas.adapter = adapter
//                binding.recyclerPersonasEncontradas.layoutManager =
//                    LinearLayoutManager(requireActivity().getApplicationContext())
//                adapter.notifyItemInserted(adapter.itemCount)
//                mNearbyDevicesArrayAdapter!!.add(msgBody)
            }

            /**
             * Función que recibe un mensaje del Listener de pérdida
             * @param message mensaje que encuentra el listener
             */
            override fun onLost(message: Message) {
                // Called when a message is no longer detectable nearby.
                val msgBody = String(message.content)
//                mNearbyDevicesArrayAdapter!!.remove(msgBody)
            }
        }
//        val nearbyDevicesArrayList: List<String> = ArrayList()
//        mNearbyDevicesArrayAdapter = ArrayAdapter(
//            requireContext(),
//            android.R.layout.simple_list_item_1,
//            nearbyDevicesArrayList
//        )
//        val nearbyDevicesListView = view.findViewById<View>(R.id.nearby_devices_list_view) as ListView
//        if (nearbyDevicesListView != null) {
//            nearbyDevicesListView.adapter = mNearbyDevicesArrayAdapter
//        }

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
     * Función que se suscribe a los mensajes de los dispositivos cercanos y actualiza la interfaz de usuario
     * si la suscripción falla o si se produce un TTL (Time To Live).
     */
    private fun subscribe() {
        Log.i(TAG, "Subscribing")
//        mNearbyDevicesArrayAdapter!!.clear()
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
     * Función que publica un mensaje a los dispositivos cercanos y actualiza la UI si la publicación falla o TTLs.
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
     * Función que deja de suscribirse a los mensajes de los dispositivos cercanos.
     */
    private fun unsubscribe() {
        Log.i(TAG, "Unsubscribing.")
        Nearby.getMessagesClient(requireActivity()).unsubscribe(mMessageListener!!)
    }

    /**
     * Función que detiene la publicación de mensajes a los dispositivos cercanos.
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
        /** ESTO QUE ES XD **/
        private val TAG = MainActivity::class.java.simpleName

        /** Cantidad de segundos que tendremos el mensaje o la suscripción vivos **/
        private const val TTL_IN_SECONDS = 10

        /** Establece el tiempo en segundos para que un mensaje publicado o una suscripción estén vivos. **/
        private val PUB_SUB_STRATEGY = Strategy.Builder().setTtlSeconds(TTL_IN_SECONDS).build()

        /** Mesaje de error si la API KEY no se encuentra **/
        private const val MISSING_API_KEY = "It's possible that you haven't added your API-KEY. See  " +
                "https://developers.google.com/nearby/messages/android/get-started#step_4_configure_your_project"
    }

    /**
     * Función que refresca el recycler con los nuevos usuarios que encuentra o que elimina.
     */
    private fun refreshFragment() {
        db.collection("Usuarios").document(Firebase.auth.currentUser!!.email.toString()).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
            }
            querySnapshot.let {
                val usuarioActualizado = it?.toObject(Usuario::class.java)!!
                if (usuarioActualizado!=null){
                    listaUsuarios = usuarioActualizado!!.usuariosEncontrados
                    adapter = RecyclerUsuariosEncontradosAdapter(this.context, listaUsuarios)
                    binding.recyclerPersonasEncontradas.adapter = adapter
                    binding.recyclerPersonasEncontradas.layoutManager = LinearLayoutManager(this.context)
                    adapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}