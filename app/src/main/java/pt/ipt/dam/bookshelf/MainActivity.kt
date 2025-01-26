package pt.ipt.dam.bookshelf

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.databinding.ActivityMainBinding
import pt.ipt.dam.bookshelf.ui.components.camera_component.CameraFragment
import pt.ipt.dam.bookshelf.ui.home_component.HomeFragment
import pt.ipt.dam.bookshelf.ui.user_profile_component.user_profile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        binding.apply {
            clickListeners()
        }
    }

    private fun clickListeners() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_1 -> {
                    val selectedFragment = CameraFragment()
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
                    val selectedFragment = user_profile()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                R.id.item_5 -> {
                    val selectedFragment = CameraFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
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
            Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show()
            // Feche o PopupWindow
            popupWindow.dismiss()
        }

        option2.setOnClickListener {
            Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
            // Feche o PopupWindow
            popupWindow.dismiss()
        }
    }
}
