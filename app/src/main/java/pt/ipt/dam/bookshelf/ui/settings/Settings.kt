package pt.ipt.dam.bookshelf.ui.settings

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.res.ResourcesCompat
import pt.ipt.dam.bookshelf.R

class Settings : Fragment() {

    companion object {
        fun newInstance() = Settings()
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Localiza os elementos do layout
        val editProfile = view.findViewById<View>(R.id.edit_profile)
        val privacy = view.findViewById<View>(R.id.privacy)
        val countRemove = view.findViewById<View>(R.id.count_remove)
        val about = view.findViewById<View>(R.id.about)

        // Configura os cliques nos elementos
        editProfile.setOnClickListener {
            showPopupEditProfile("Editar Perfil", "Aqui você pode editar seu perfil, alterar a foto e as informações pessoais.")
        }
        privacy.setOnClickListener {
            showPopupPrivacy("Política de Privacidade", "Aqui você pode consultar todos os dados que coletamos e como os usamos.")
        }
        countRemove.setOnClickListener {
            showPopupDelete("Eliminar Conta", "Tem certeza que deseja excluir sua conta? Essa ação não pode ser desfeita.")
        }
        about.setOnClickListener {
            showPopupSobre("Sobre Nós", "Curso: Engenharia Informática\nAutoria: António Gonçalves 23787\nAfonso Costa 24855\nBibliotecas")
        }

        return view
    }

    private fun showPopupEditProfile(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setOnShowListener {
            // Obtém as views do título e da mensagem
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)

            // Altera a fonte (fontFamily)
            titleTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_semi_bold)
            messageTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)

            // Modifica o tamanho do texto
            titleTextView?.textSize = 40f
            messageTextView?.textSize = 16f
        }

        // Aplica o fundo com cantos arredondados
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_sobre)

        // Exibe o dialog
        dialog.show()
    }

    private fun showPopupPrivacy(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setOnShowListener {
            // Obtém as views do título e da mensagem
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)

            // Altera a fonte (fontFamily)
            titleTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_semi_bold)
            messageTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)

            // Modifica o tamanho do texto
            titleTextView?.textSize = 40f
            messageTextView?.textSize = 16f
        }

        // Aplica o fundo com cantos arredondados
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_sobre)

        // Exibe o dialog
        dialog.show()
    }

    private fun showPopupDelete(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Confirmar") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setOnShowListener {
            // Obtém as views do título e da mensagem
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)

            // Altera a fonte (fontFamily)
            titleTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_semi_bold)
            messageTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)

            // Modifica o tamanho do texto
            titleTextView?.textSize = 40f
            messageTextView?.textSize = 16f
        }

        // Aplica o fundo com cantos arredondados
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_sobre)

        // Exibe o dialog
        dialog.show()
    }

    private fun showPopupSobre(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setOnShowListener {
            // Obtém as views do título e da mensagem
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)

            // Altera a fonte (fontFamily)
            titleTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_semi_bold)
            messageTextView?.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)

            // Modifica o tamanho do texto
            titleTextView?.textSize = 40f
            messageTextView?.textSize = 16f
        }

        // Aplica o fundo com cantos arredondados
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_sobre)

        // Exibe o dialog
        dialog.show()
    }
}