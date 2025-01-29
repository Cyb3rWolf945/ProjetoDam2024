package pt.ipt.dam.bookshelf.ui.settings

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
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment

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
        val deleteAccount = view.findViewById<View>(R.id.count_remove)
        val privacy = view.findViewById<View>(R.id.privacy)


        privacy.setOnClickListener {
            val url = "https://staticdam.onrender.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Configura os cliques nos elementos
        editProfile.setOnClickListener {
            showPopupEditProfile(
                "Editar Perfil",
                "Aqui você pode editar seu perfil, alterar a foto e as informações pessoais."
            )
        }

        about.setOnClickListener {
            showPopupSobre(
                "Sobre Nós",
                "Curso: Engenharia Informática\nAutoria: António Gonçalves 23787\nAfonso Costa 24855\nBibliotecas"
            )
        }

        // Adiciona o clique para deletar a conta
        deleteAccount.setOnClickListener {
            showPopupDeleteUser(
                "Deletar Conta",
                "Você realmente deseja excluir sua conta? Essa ação não pode ser revertida."
            )
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
            Toast.makeText(context, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
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
}