package pt.ipt.dam.bookshelf.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.databinding.SplashScreenLayoutBinding
import pt.ipt.dam.bookshelf.ui.auth.authActivity
import java.util.Locale

/***
 * Classe de atividade do SplashScreen
 * Esta classe responsabiliza-se por:
 * Ser a tela de abertura da aplicação com um delay pevisto de 2 segundos.
 * Ir confirmar se o utilizador esta auenticado, pelo facto do seu objeto estar no SharedPreferences ou não.
 * E define o local por padrão caso o mesmo não exista.
 */
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: SplashScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        val savedLanguage = UserPreferences.getLocale(this)
        if (savedLanguage.isNotEmpty()) {
            setLocale(savedLanguage)
        }

        splashScreen.setKeepOnScreenCondition {
            false
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = SplashScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        UserPreferences.init(this)

        val user = UserPreferences.getUser()

        if (user != null) {
            // existe o first e o secod porque é um par de valores: Pair<Int, String>?
            if (savedLanguage == "pt"){
                binding.splashWelcomeBack.text = "Olá ${user.second}"
            }else{
                binding.splashWelcomeBack.text = "Hello ${user.second}"
            }
        } else {
            binding.splashWelcomeBack.text = ""
        }


        binding.root.postDelayed({
            if (user != null) {
                // Se o utilizador estiver autenticado, vai para a MainActivity
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("userId", user.first)
                    putExtra("userName", user.second)
                }
                startActivity(intent)

            } else {
                // Se não estiver autenticado, vai para a AuthActivity
                startActivity(Intent(this, authActivity::class.java))
            }
            finish()
        }, 2000)
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