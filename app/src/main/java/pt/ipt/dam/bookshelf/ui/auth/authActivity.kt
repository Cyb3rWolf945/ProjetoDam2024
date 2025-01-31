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

class authActivity : AppCompatActivity() {

    // Declare the binding variable
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val savedLanguage = UserPreferences.getLocale(this)
        if (savedLanguage.isNotEmpty()) {
            setLocale(savedLanguage)
        }

        val call = RetrofitClient.client
        // Initialize the binding
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use the root view from the binding

        // Handling edge to edge insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the Login fragment by default if there's no saved instance
        if (savedInstanceState == null) {
            loadFragment(loginFragment()) // Ensure LoginFragment is your first fragment
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the fragment container with the specified fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
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
}
