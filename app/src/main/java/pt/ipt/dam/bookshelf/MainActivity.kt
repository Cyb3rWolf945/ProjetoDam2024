package pt.ipt.dam.bookshelf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import pt.ipt.dam.bookshelf.databinding.ActivityMainBinding
import pt.ipt.dam.bookshelf.ui.components.camera_component.CameraFragment
import pt.ipt.dam.bookshelf.ui.user_profile_component.user_profile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Seleção da opção 3 do menu e inserção do fragmento no UI.
        binding.bottomNavigation.selectedItemId = R.id.item_3
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CameraFragment()) // Certifique-se de que 'fragment_container' seja o ID do FrameLayout no layout principal
            .commit()


        binding.apply {
            clickListeners()
        }


    }

    private fun clickListeners() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            val selectedFragment = when(menuItem.itemId) {
                R.id.item_1 -> CameraFragment()
                R.id.item_2 -> CameraFragment()
                R.id.item_3 -> CameraFragment() // camera
                R.id.item_4 -> user_profile() // user
                R.id.item_5 -> CameraFragment()
                else -> null
            }

            if (selectedFragment != null) {
                // Substituir o fragmento
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment) // Certifique-se de que 'fragment_container' seja o ID do FrameLayout no layout principal
                    .commit()
            }

            return@setOnItemSelectedListener true
        }
    }


}