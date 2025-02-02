package pt.ipt.dam.bookshelf.ui.auth

import android.content.Context
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.databinding.ActivityAuthBinding // Import the binding class
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment
import java.util.Locale

/***
 * Classe responsavel pela atividade de autenticação, que é responsavel exatamente pelas páginas de login e registo.
 */
class authActivity : AppCompatActivity() {

    // Usar View Binding
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val savedLanguage = UserPreferences.getLocale(this)
        if (savedLanguage.isNotEmpty()) {
            setLocale(savedLanguage)
        }

        val call = RetrofitClient.client


        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Assegurar que o loginFragment apareça por defeito
        if (savedInstanceState == null) {
            loadFragment(loginFragment())
        }
    }

    /***
     * Esta função esta responsavél por tratar da transição do fragment.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    /****
     * Função responsavel por dar set do Locale a partir das sharedPreferences.
     */
    private fun setLocale(localeToSet: String) {
        val localeListToSet = LocaleList(Locale(localeToSet))
        LocaleList.setDefault(localeListToSet)
        resources.configuration.setLocales(localeListToSet)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        sharedPref.putString("locale_to_set", localeToSet)
        sharedPref.apply()
    }
}
