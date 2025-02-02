package pt.ipt.dam.bookshelf.ui.books.collections.collectionDetails

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentCollectionDetailsBinding
import pt.ipt.dam.bookshelf.models.LivrosResponse

/***
 * Classe responsavél pelo Fragmento de detalhe de coleções.
 * Exibe os livros de uma coleção.
 * E inclui a funcionalidade de abanar o telemovel para mostrar um livro aleatório.
 */
class CollectionDetailsFragment : Fragment(), SensorEventListener {

    private val viewModel: CollectionDetailsViewModel by viewModels()
    private var _binding: FragmentCollectionDetailsBinding? = null
    private lateinit var savedLanguage: String
    private val binding get() = _binding!!

    // var para o sensor acelarometro.
    private var isSensorActive = false // variavel de controlo
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var lastUpdate: Long = 0
    private var shakeCount = 0  // Contador de vezes que o aparelho foi abanado.
    private val REQUIRED_SHAKES = 3  // Número de shakes necessários para abrir o dialog.
    private val shakeThreshold = 1000  // Limite de aceleração necessário para um shake
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
        savedLanguage = UserPreferences.getLocale(requireContext())
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        isSensorActive = false
        binding.toggleSensorButton.isEnabled = true
    }

    /***
     * Função do acelarometro para quando os valores X,Y,Z mudam.
     */
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

                // calculo da acelaração linear
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

    /***
     * O sensor é ativado através do botão portanto, ao clicar no mesmo temos de dar reset nas variaveis
     * X,Y,Z de forma a poder ser calculada novamente a acelaração.
     * Bem como é dado o reset do número de shakes dado para que se tenha de realizar os 3 shakes outra vez.
     */
    private fun startSensor() {
        binding.toggleSensorButton.isEnabled = false
        if (!isSensorActive && booksAdapter.getItemCount() > 0) {
            isSensorActive = true
            shakeCount = 0
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


    /***
     * Dialog caso abanes 3 vezes o mobile. (bastante divertido)
     */
    private fun showRandomBookDialog() {
        val randomBook = booksAdapter.getRandomBook()
        if (randomBook != null) {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_book_info, null)

            val coverImageView = dialogView.findViewById<ImageView>(R.id.coverImageView)
            val ratingTextView = dialogView.findViewById<TextView>(R.id.ratingTextView)
            val pagesTextView = dialogView.findViewById<TextView>(R.id.pagesTextView)
            val dimensionsTextView = dialogView.findViewById<TextView>(R.id.dimensionsTextView)
            val buttonTextAccept = if (savedLanguage == "pt") "Ler" else "Read"
            val buttonTextCancel = if (savedLanguage == "pt") "Cancelar" else "Cancel"

            val imageUrl = randomBook.url.replace("http:", "https:")
            coverImageView.load(imageUrl) {
                crossfade(true)
                error(R.drawable.ic_launcher_background)
            }

            // Define os textos com os dados do livro
            ratingTextView.text = randomBook.rating?.let { String.format("%.1f", it) } ?: "N/A"
            pagesTextView.text = randomBook.paginas?.toString() ?: "N/A"
            dimensionsTextView.text = "A4"

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(randomBook.titulo)
                .setView(dialogView)
                .setPositiveButton(buttonTextAccept) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(buttonTextCancel) { dialog, _ ->
                    dialog.dismiss()
                }
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