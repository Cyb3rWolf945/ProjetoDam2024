package pt.ipt.dam.bookshelf.ui.settings

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.res.ResourcesCompat
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment
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
                "Deletar Conta",
                "Tem a certeza que quer apagar a sua conta?"
            )
        }


        viewModel.updateSuccess.observe(viewLifecycleOwner, { success ->
            if (success) {
                Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao atualizar os dados.", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    private fun showPopupEditProfile(title: String, message: String) {
        val context = requireContext()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)

        val editName = dialogView.findViewById<EditText>(R.id.editTextNome)
        val editEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editPassword = dialogView.findViewById<EditText>(R.id.editTextPassword)
        val editApelido = dialogView.findViewById<EditText>(R.id.editTextApelido)


        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = preferences.getInt("userid", -1)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Alterar os Dados") { _, _ ->
                val name = editName.text.toString()
                val email = editEmail.text.toString()
                val password = editPassword.text.toString()
                val apelido = editApelido.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    if (isValidPassword(password) && isValidEmail(email)){
                        viewModel.updateUser(name, email, password, apelido)
                    } else {
                        Toast.makeText(requireContext(), "Todos os campos devem ser preenchidos corretamente!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(
                        context,
                        "Todos os campos devem ser preenchidos!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showPopupSobre(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setOnShowListener {
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)

            titleTextView?.textSize = 40f
            messageTextView?.textSize = 16f
        }

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_sobre)
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
}