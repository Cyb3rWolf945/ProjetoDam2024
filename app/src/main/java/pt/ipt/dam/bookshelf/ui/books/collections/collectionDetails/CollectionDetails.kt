package pt.ipt.dam.bookshelf.ui.books.collections.collectionDetails

import android.app.AlertDialog
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentCollectionDetailsBinding
import pt.ipt.dam.bookshelf.models.LivrosResponse

class CollectionDetailsFragment : Fragment(), SensorEventListener {

    private val viewModel: CollectionDetailsViewModel by viewModels()
    private var _binding: FragmentCollectionDetailsBinding? = null
    private val binding get() = _binding!!

    // var para o sensor do giroscopio
    private var isSensorActive = false // controlo do sensor
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var lastUpdate: Long = 0
    private var shakeCount = 0  // Add this property
    private val REQUIRED_SHAKES = 3  // Add this property
    private val shakeThreshold = 1000  // Increased threshold
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    private lateinit var booksAdapter: BooksAdapter
    val books: MutableList<LivrosResponse> = mutableListOf()

    private var collectionId: Int = -1
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recebe o ID da coleção e do utilizador
        arguments?.let {
            collectionId = it.getInt("idcolecoes")
            userId = it.getInt("userId")
        }

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) // Use o acelerômetro
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with an empty list first
        booksAdapter = BooksAdapter(books.toMutableList())
        setupRecyclerView()

        // Set the adapter to the RecyclerView
        binding.recyclerViewBooks.adapter = booksAdapter

        binding.toggleSensorButton.setOnClickListener {
            startSensor()
        }

        // Fetch and observe books
        viewModel.fetchBooksForCollection(userId, collectionId)
        viewModel.books.observe(viewLifecycleOwner) { books ->
            if (books != null) {
                booksAdapter = BooksAdapter(books.toMutableList())
                binding.recyclerViewBooks.adapter = booksAdapter

                binding.missingBooks.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
                binding.toggleSensorButton.visibility = if (books.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lastUpdate = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        isSensorActive = false
        binding.toggleSensorButton.isEnabled = true
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastUpdate) > 500) { // Increased from 100 to 500

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val deltaX = x - lastX
                val deltaY = y - lastY
                val deltaZ = z - lastZ

                val speed = Math.abs(deltaX + deltaY + deltaZ) / ((currentTime - lastUpdate)) * 10000

                if (speed > shakeThreshold) {
                    shakeCount++
                    if (shakeCount >= REQUIRED_SHAKES) {
                        sensorManager.unregisterListener(this)
                        isSensorActive = false
                        shakeCount = 0  // Reset counter
                        showRandomBookDialog()
                    }
                }

                lastX = x
                lastY = y
                lastZ = z
                lastUpdate = currentTime
            }
        }
    }

    private fun startSensor() {
        binding.toggleSensorButton.isEnabled = false
        if (!isSensorActive && booksAdapter.getItemCount() > 0) {
            isSensorActive = true
            shakeCount = 0  // Reset counter when starting
            lastX = 0f
            lastY = 0f
            lastZ = 0f
            lastUpdate = System.currentTimeMillis()
            sensor?.also { accel ->
                sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI)
            }
            binding.toggleSensorButton.isEnabled = false
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Não necessário para este exemplo
    }

    private fun showRandomBookDialog() {
        val randomBook = booksAdapter.getRandomBook()
        if (randomBook != null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Livro Aleatório")
                .setMessage("Livro Selecionado: ${randomBook.titulo}")
                .setPositiveButton("Fechar") { dialog, _ ->
                    dialog.dismiss()
                    binding.toggleSensorButton.isEnabled = true
                }
                .create()
                .show()
        }
    }



    private fun setupRecyclerView() {
        binding.recyclerViewBooks.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}