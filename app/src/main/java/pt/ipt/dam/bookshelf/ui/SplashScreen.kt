package pt.ipt.dam.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.databinding.SplashScreenLayoutBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: SplashScreenLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()


        splashScreen.setKeepOnScreenCondition {
            false // Set to `true` to keep splash screen active longer
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = SplashScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.root.postDelayed({
            // You can start your next activity after the splash screen
            startActivity(Intent(this, MainActivity::class.java)) // Change to your target activity
            finish() // Close the splash activity to avoid going back to it
        }, 2000) // 2-second delay for the splash screen, adjust as needed
    }
}