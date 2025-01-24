package pt.ipt.dam.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.databinding.SplashScreenLayoutBinding
import pt.ipt.dam.bookshelf.ui.auth.authActivity
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



        // Verificação de utilizador em cache
        val user: User? = UserCacheUtil.readUserFromCache(this)

        binding.root.postDelayed({
            if (user != null) {
                // Se o utilizador estiver autenticado é redirecionado para a main activity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Se o utilizador não estiver autenticado é redirecionado para a auth activity
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish() // Temos de fechar o splash screen para evitar voltar atrás na pilha.
        }, 2000) // delay de 2 segundos.
    }
}