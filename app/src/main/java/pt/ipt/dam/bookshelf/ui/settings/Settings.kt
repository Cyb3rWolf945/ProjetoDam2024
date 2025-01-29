package pt.ipt.dam.bookshelf.ui.settings

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.res.ResourcesCompat
import pt.ipt.dam.bookshelf.R

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

        // Localiza os elementos do layout
        val editProfile = view.findViewById<View>(R.id.edit_profile)
        val about = view.findViewById<View>(R.id.about)

        // Configura os cliques nos elementos
        editProfile.setOnClickListener {
            showPopupEditProfile("Editar Perfil", "Aqui você pode editar seu perfil, alterar a foto e as informações pessoais.")
        }

        about.setOnClickListener {
            showPopupSobre("Sobre Nós", "Curso: Engenharia Informática\nAutoria: António Gonçalves 23787\nAfonso Costa 24855\nBibliotecas")
        }

        // Observa o resultado da atualização do usuário
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

        // Obtém o userId das SharedPreferences
        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = preferences.getInt("userid", -1)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Alterar os Dados") { _, _ ->
                val name = editName.text.toString()
                val email = editEmail.text.toString()
                val password = editPassword.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    // Atualiza o usuário
                    viewModel.updateUser(name, email, password)
                } else {
                    Toast.makeText(context, "Todos os campos devem ser preenchidos!", Toast.LENGTH_SHORT).show()
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
}