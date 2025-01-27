package pt.ipt.dam.bookshelf

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import pt.ipt.dam.bookshelf.databinding.ActivityMainBinding
import pt.ipt.dam.bookshelf.models.VolumeInfo
import pt.ipt.dam.bookshelf.ui.home_component.HomeFragment
import pt.ipt.dam.bookshelf.ui.user_profile_component.user_profile
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
        }
    }

    private fun clickListeners() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_1 -> {

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
                    val selectedFragment = user_profile()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                R.id.item_5 -> {

                }
            }
            return@setOnItemSelectedListener true
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
            Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
            // Feche o PopupWindow
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
        MaterialAlertDialogBuilder(this)
            .setTitle(bookInfo.title)
            .setMessage("Author(s): ${bookInfo.authors?.joinToString(", ")}\n" +
                    "Description: ${bookInfo.description}\n" +
                    "Published: ${bookInfo.publishedDate}")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.book_not_found)
            .setMessage(message)
            .setNegativeButton("Cancel", null)
            .show()
    }
}
