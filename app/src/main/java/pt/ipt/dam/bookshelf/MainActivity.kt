package pt.ipt.dam.bookshelf

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.LocaleList
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
import pt.ipt.dam.bookshelf.ui.settings.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import pt.ipt.dam.bookshelf.ui.user_related.searchUsers
import java.util.Locale
import java.util.concurrent.ExecutorService

/***
 * Classe da atividade principal da aplicação.
 * Está classe está responsavel por ser a classe da atividade mãe para todos os fragmentos.
 * (sempre que um fragmento precisa de um context, vem buscar a esta classe).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService
    private var userId: Int = -1
    private var userName: String? = null
    private lateinit var savedLanguage: String

    // ViewModel responsavel pelo dialog de adição do livro
    private lateinit var viewModel: BookInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        savedLanguage = UserPreferences.getLocale(this)
        if (savedLanguage.isNotEmpty()) {
            setLocale(savedLanguage)
        }

        // se user não existir no sharedpreferences - manda para o login.

        userId = intent.getIntExtra("userId", -1)
        userName = intent.getStringExtra("userName")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Seleção da opção 2 do menu e inserção do fragmento no UI.
        //val teste = RetrofitClient.getClient().

        //Seleção da opção 1 do menu e inserção do fragmento no UI.
        binding.bottomNavigation.selectedItemId = R.id.item_1
        val selectedFragment = Collections()

        val bundle = Bundle()
        bundle.putInt("userId", userId)
        bundle.putString("userName", userName)
        selectedFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()


        binding.bottomNavigation.menu.findItem(R.id.item_2)?.isVisible = false

        viewModel = ViewModelProvider(this).get(BookInfoViewModel::class.java)

        /***
         * Observable para a variavel bookInfoState.
         */
        viewModel.bookInfoState.observe(this, Observer { state ->
            when (state) {
                is BookInfoState.Loading -> {
                    //showLoadingDialog()
                }
                is BookInfoState.Success -> {
                    showSuccessDialog(state.bookInfo)
                }
                is BookInfoState.Error -> {
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

                    val bundle = Bundle()
                    bundle.putInt("userId", userId)
                    bundle.putString("userName", userName)
                    selectedFragment.arguments = bundle

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                R.id.item_2 -> {
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

        // Defina o comportamento dos itens do menu aqui.
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

    /***
     * Permissões de camara
     */
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


    /***
     * Esta função vai iniciar a camara a partir do GMSBarCodeScanner.
     * Em caso de leitura com sucesso -> Faz fetch dos dados.
     * EM caso de cancelar leitura não faz nada.
     * EM caso de erro leitura não faz nada.
     */
    private fun startCamera() {

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,  // ISBN em formato EAN-13
                Barcode.FORMAT_UPC_A)  // ISBN em formato UPC-A
            .enableAutoZoom()
            .build()

        val barcodeScanner = GmsBarcodeScanning.getClient(this, options)

        // Inicio do scan
        barcodeScanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                val rawValue = barcode.rawValue // Retrieve the raw value of the barcode
                // Do something with the rawValue (like displaying or processing)
                if (rawValue != null) {
                    viewModel.fetchBookInfo(rawValue, BuildConfig.BOOKS_API_KEY)
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


    private fun setLocale(localeToSet: String) {
        val localeListToSet = LocaleList(Locale(localeToSet))
        LocaleList.setDefault(localeListToSet)
        resources.configuration.setLocales(localeListToSet)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        sharedPref.putString("locale_to_set", localeToSet)
        sharedPref.apply()
    }



    //Dialogs -- Para adição de livros
    private fun showSuccessDialog(bookInfo: VolumeInfo) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book_info, null)

        val coverImageView = dialogView.findViewById<ImageView>(R.id.coverImageView)
        val ratingTextView = dialogView.findViewById<TextView>(R.id.ratingTextView)
        val pagesTextView = dialogView.findViewById<TextView>(R.id.pagesTextView)
        val dimensionsTextView = dialogView.findViewById<TextView>(R.id.dimensionsTextView)

        val buttonTextAccept = if (savedLanguage == "pt") "Adicionar" else "Add"
        val buttonTextCancel = if (savedLanguage == "pt") "Cancelar" else "Cancel"


        val imageUrl = bookInfo.imageLinks?.thumbnail?.replace("http:", "https:")
        coverImageView.load(imageUrl) {
            crossfade(true)
            error(R.drawable.ic_launcher_background)
        }


        ratingTextView.text = bookInfo.averageRating?.let { String.format("%.1f", it) } ?: "N/A"
        pagesTextView.text = bookInfo.pageCount?.toString() ?: "N/A"
        dimensionsTextView.text = "A4"

        MaterialAlertDialogBuilder(this)
            .setTitle(bookInfo.title)
            .setView(dialogView)
            .setPositiveButton(buttonTextAccept) { dialog, _ ->
                addBookToCollection(bookInfo)
                binding.bottomNavigation.selectedItemId = R.id.item_1
            }
            .setNegativeButton(buttonTextCancel) { dialog, _ ->
                dialog.dismiss()
                binding.bottomNavigation.selectedItemId = R.id.item_1

            }
            .show()
    }

    private fun addBookToCollection(bookInfo: VolumeInfo) {
        val retrofit = RetrofitClient.client
        val bookApi = retrofit.create(Service::class.java)

        // Extrair ISBN corretamente
        val isbn = bookInfo.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier
            ?: bookInfo.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier
            ?: ""

        // Converter lista de autores para string
        val autor = bookInfo.authors?.joinToString(", ") ?: "Autor desconhecido"

        // Criar objeto Livros compatível com o backend
        val livro = Livros(
            nome = bookInfo.title ?: "Título Desconhecido",
            dataemissao = bookInfo.publishedDate ?: "",
            autor = autor,
            descricao = bookInfo.description ?: "",
            rating = bookInfo.averageRating?.toFloat() ?: 0f,
            ISBN = isbn,
            paginas = bookInfo.pageCount ?: 0,
            url = bookInfo.imageLinks?.thumbnail ?: ""
        )

        // Verificar se o userId é válido
        if (userId == -1) {
            Log.e("AddBook", "Invalid user ID")
            return
        }

        // Chamar a API corretamente
        val call = bookApi.addBook(userId, livro)
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

    fun setBottomNavigationItem(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }
}
