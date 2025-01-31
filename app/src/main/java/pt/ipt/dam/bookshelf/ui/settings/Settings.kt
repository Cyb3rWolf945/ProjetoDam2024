package pt.ipt.dam.bookshelf.ui.settings

import UserPreferences.clearUser
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.ui.auth.authActivity
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment
import pt.ipt.dam.bookshelf.utils.ToastUtils
import java.util.Locale
import java.util.regex.Pattern

class Settings : Fragment() {

    companion object {
        fun newInstance() = Settings()
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)


        val editProfile = view.findViewById<View>(R.id.edit_profile)
        val about = view.findViewById<View>(R.id.about)
        val deleteAccount = view.findViewById<View>(R.id.count_remove)
        val privacy = view.findViewById<View>(R.id.privacy)
        val session = view.findViewById<View>(R.id.close_session)
        val change_language = view.findViewById<View>(R.id.change_language)


        session.setOnClickListener{
            clearUser()
            val intent = Intent(requireContext(), authActivity::class.java)
            startActivity(intent)
        }

        change_language.setOnClickListener{
            val languages = arrayOf("Português", "English")  // Only Portuguese and English
            val langSelectorBuilder = AlertDialog.Builder(requireContext())
            langSelectorBuilder.setTitle("Choose language:")
            langSelectorBuilder.setSingleChoiceItems(languages, -1) { dialog, selection ->
                when (selection) {
                    0 -> {
                        setLocale("pt")  // Set to Portuguese
                        (activity as? MainActivity)?.setBottomNavigationItem(R.id.item_1)
                    }
                    1 -> {
                        setLocale("en")  // Set to English
                        (activity as? MainActivity)?.setBottomNavigationItem(R.id.item_1)
                    }
                }
                dialog.dismiss()
            }
            langSelectorBuilder.create().show()

        }


        privacy.setOnClickListener {
            val url = "https://staticdam.onrender.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


        editProfile.setOnClickListener {
            showPopupEditProfile(
                "Editar Perfil",
                ""
            )
        }

        about.setOnClickListener {
            showPopupSobre(
                "Sobre Nós",
                "Curso: Engenharia Informática\nAutoria: António Gonçalves 23787\nAfonso Costa 24855\nBibliotecas"
            )
        }


        deleteAccount.setOnClickListener {
            showPopupDeleteUser(
                "Eliminar Conta",
                "Tem a certeza que quer apagar a sua conta?"
            )
        }


        return view
    }

    private fun showPopupEditProfile(title: String, message: String) {
        val context = requireContext()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)

        val editName = dialogView.findViewById<EditText>(R.id.editTextNome)
        val editEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editPassword = dialogView.findViewById<EditText>(R.id.editTextPassword)
        val editApelido = dialogView.findViewById<EditText>(R.id.editTextApelido)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSave.setOnClickListener {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val apelido = editApelido.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (isValidPassword(password) && isValidEmail(email)) {
                    viewModel.updateUser(name, email, password, apelido)
                    ToastUtils.showCustomToast(requireContext(), "Dados atualizados com sucesso!")
                    dialog.dismiss()
                } else {
                    ToastUtils.showCustomToast(requireContext(), "Todos os campos devem ser preenchidos corretamente")
                }
            } else {
                ToastUtils.showCustomToast(requireContext(), "Todos os campos devem ser preenchidos")
            }
        }

        dialog.show()
    }

    private fun showPopupSobre(title: String, message: String) {
        val context = requireContext()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sobre, null)

        val textMessage = dialogView.findViewById<TextView>(R.id.textMessage)
        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)


        textMessage.text = message

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        buttonOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showPopupDeleteUser(title: String, message: String) {
        val context = requireContext()


        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val userId = preferences.getInt("userid", -1)
        if (userId == -1) {
            Toast.makeText(context, "Utilizador não encontrado.", Toast.LENGTH_SHORT).show()
            return
        }
        val dialogView = LayoutInflater.from(context).inflate(R.layout.delete_account, null)
        val buttonDelete = dialogView.findViewById<Button>(R.id.buttonDelete)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .create()
        buttonDelete.setOnClickListener {
            viewModel.deleteUser(userId)
            dialog.dismiss()

            ToastUtils.showCustomToast(requireContext(), "Conta apagada com sucesso!")

            val selectedFragment = loginFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
        }


        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    //estas funções sao dedicadas ao regex do email e da password
    fun isValidEmail(email: String) : Boolean {
        return Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$").matcher(password).matches()
    }

    private fun setLocale(languageCode: String) {
        // Save language in SharedPreferences
        UserPreferences.saveLocale(languageCode, requireContext())

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = requireContext().resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Restart current activity
        requireActivity().recreate()
    }
}