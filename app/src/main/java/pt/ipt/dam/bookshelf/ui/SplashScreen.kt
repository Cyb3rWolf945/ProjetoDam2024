package pt.ipt.dam.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.databinding.SplashScreenLayoutBinding
import pt.ipt.dam.bookshelf.ui.auth.authActivity
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment
import pt.ipt.dam.bookshelf.utils.User
import pt.ipt.dam.bookshelf.utils.UserCacheUtil

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: SplashScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            false
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = SplashScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

       //sharedpreferences
        UserPreferences.init(this)


        val user = UserPreferences.getUser()

        if (user != null) {
            // existe o first e o secod porque é um par de valores: Pair<Int, String>?
            binding.splashWelcomeBack.text = "Olá ${user.second}"
        } else {
            binding.splashWelcomeBack.text = ""
        }


        binding.root.postDelayed({
            if (user != null) {
                // Se o utilizador estiver autenticado, vai para a MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Se não estiver autenticado, vai para a AuthActivity
                startActivity(Intent(this, authActivity::class.java))
            }
            finish()
        }, 2000)
    }
}