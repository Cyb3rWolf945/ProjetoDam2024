package pt.ipt.dam.bookshelf

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.databinding.ActivityMainBinding
import pt.ipt.dam.bookshelf.models.Livros
import pt.ipt.dam.bookshelf.models.VolumeInfo
import pt.ipt.dam.bookshelf.searchBooks.search_books
import pt.ipt.dam.bookshelf.ui.books.collections.Collections
import pt.ipt.dam.bookshelf.ui.home_component.HomeFragment
import pt.ipt.dam.bookshelf.ui.settings.Settings
import pt.ipt.dam.bookshelf.ui.user_profile_component.user_profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import pt.ipt.dam.bookshelf.ui.user_related.searchUsers
import java.util.concurrent.ExecutorService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService

    // ViewModel responsavel pelo dialog de adição do livro
    private lateinit var viewModel: BookInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Seleção da opção 2 do menu e inserção do fragmento no UI.
        //val teste = RetrofitClient.getClient().

        //Seleção da opção 3 do menu e inserção do fragmento no UI.
        binding.bottomNavigation.selectedItemId = R.id.item_2
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment()) // O 'fragment_container' terá de ser o ID do FrameLayout no layout principal
            .commit()


        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(BookInfoViewModel::class.java)

        // Observe LiveData
        viewModel.bookInfoState.observe(this, Observer { state ->
            when (state) {
                is BookInfoState.Loading -> {
                    // Show loading dialog or progress
                    //showLoadingDialog()
                }
                is BookInfoState.Success -> {
                    // Update the dialog with book information
                    showSuccessDialog(state.bookInfo)
                }
                is BookInfoState.Error -> {
                    // Show error message
                    showErrorDialog(state.message)
                }
            }
        })

        binding.apply {
            clickListeners()
            clickListenersTopBar()
        }
    }

    private fun clickListeners() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_1 -> {
                    val selectedFragment = Collections()
                    val selectedFragment = searchUsers()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                R.id.item_2 -> {
                    val selectedFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                R.id.item_3 -> {
                    // Criar e mostrar o PopupMenu para a opção 3
                    showPopupMenu(menuItem)
                }
                R.id.item_4 -> {
                    val selectedFragment = searchUsers()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                R.id.item_5 -> {
                    val selectedFragment = Settings()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()

                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun clickListenersTopBar(){
        binding.topAppBar.avatar.setOnClickListener{
            val selectedFragment = Settings()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
        }
    }

    private fun showPopupMenu(menuItem: MenuItem) {
        // Criação da View para o PopupWindow
        val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.custom_popup_menu, null)

        // Defina o comportamento dos itens do menu aqui
        val option1: TextView = view.findViewById(R.id.option1)
        val option2: TextView = view.findViewById(R.id.option2)

        // Criação do PopupWindow
        val popupWindow = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)

        // Ajusta a largura para a largura total da tela
        val screenWidth = resources.displayMetrics.widthPixels
        popupWindow.width = screenWidth

        // Calcula a posição do BottomNavigationView
        val bottomNavigationHeight = binding.bottomNavigation.height
        val location = IntArray(2)
        binding.bottomNavigation.getLocationOnScreen(location)
        val yPosition = location[1] - bottomNavigationHeight  // Calculando a posição acima do menu

        // Exibe o PopupWindow fora da tela (logo abaixo)
        popupWindow.showAtLocation(binding.root, Gravity.TOP, 0, yPosition)

        // Animação para mover o PopupWindow de baixo para cima
        val animation = TranslateAnimation(0f, 0f, 500f, 0f) // Começa 500px abaixo e sobe
        animation.duration = 300 // Duração da animação
        animation.fillAfter = true // Manter a posição final após a animação

        view.startAnimation(animation) // Aplica a animação na view do PopupWindow

        // Definir ações para os itens de menu
        option1.setOnClickListener {
            checkCameraPermission()
            // Feche o PopupWindow
            popupWindow.dismiss()
        }

        option2.setOnClickListener {
            val selectedFragment = search_books()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            popupWindow.dismiss()
        }
    }


    //CAMARA FUNCTIONS

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1001)
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            // Handle permission denied
            Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startCamera() {
        // Initialize barcodeScanner within the method, no global variable needed
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,  // ISBN em formato EAN-13
                Barcode.FORMAT_UPC_A)  // ISBN em formato UPC-A
            .enableAutoZoom()
            .build()

        val barcodeScanner = GmsBarcodeScanning.getClient(this, options)

        // Start barcode scanning
        barcodeScanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                val rawValue = barcode.rawValue // Retrieve the raw value of the barcode
                // Do something with the rawValue (like displaying or processing)
                if (rawValue != null) {
                    viewModel.fetchBookInfo(rawValue, "AIzaSyBJaKQAJgsSeHOOdY0uZVhufLBDZN-Ps7I")
                }
                binding.bottomNavigation.selectedItemId = R.id.item_2 // para mudar de sitio
                Toast.makeText(this, "Barcode detected: $rawValue", Toast.LENGTH_SHORT).show()

            }
            .addOnCanceledListener {
                // Task canceled
                println("Scan was canceled")
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                e.printStackTrace()
                println("Scan failed: ${e.message}")
            }
    }





    //Dialogs -- Para adição de livros
    private fun showSuccessDialog(bookInfo: VolumeInfo) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book_info, null)

        val coverImageView = dialogView.findViewById<ImageView>(R.id.coverImageView)
        val ratingTextView = dialogView.findViewById<TextView>(R.id.ratingTextView)
        val pagesTextView = dialogView.findViewById<TextView>(R.id.pagesTextView)
        val dimensionsTextView = dialogView.findViewById<TextView>(R.id.dimensionsTextView)


        val imageUrl = bookInfo.imageLinks?.thumbnail?.replace("http:", "https:")
        coverImageView.load(imageUrl) {
            crossfade(true)
            error(R.drawable.ic_launcher_background)
        }

        // Set text values using API data
        ratingTextView.text = bookInfo.averageRating?.let { String.format("%.1f", it) } ?: "N/A"
        pagesTextView.text = bookInfo.pageCount?.toString() ?: "N/A"
        dimensionsTextView.text = "A4"

        MaterialAlertDialogBuilder(this)
            .setTitle(bookInfo.title)
            .setView(dialogView)
            .setPositiveButton("Adicionar") { dialog, _ ->
                // Handle adding the book to your collection
                addBookToCollection(bookInfo)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addBookToCollection(bookInfo: VolumeInfo) {
        val retrofit = RetrofitClient.client
        val bookApi = retrofit.create(Service::class.java)

        // Extract ISBN from industryIdentifiers
        val isbn = bookInfo.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier
            ?: bookInfo.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier
            ?: ""

        // Map VolumeInfo to Livros
        val livro = Livros(
            nome = bookInfo.title ?: "Unknown Title",
            dataemissao = bookInfo.publishedDate ?: "",
            editora = 1,
            descricao = bookInfo.description ?: "",
            rating = (bookInfo.averageRating?.toFloat() ?: 0f),
            ISBN = isbn ?: "",
            paginas = bookInfo.pageCount ?: 0
        )

        // Call the API
        val call = bookApi.addBook(livro)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("AddBook", "Book added successfully!")
                } else {
                    Log.e("Error", "Failed to add book: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AddBook", "Error adding book: ${t.message}")
            }
        })
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.book_not_found)
            .setMessage(message)
            .setNegativeButton("Cancel", null)
            .show()
    }
}
