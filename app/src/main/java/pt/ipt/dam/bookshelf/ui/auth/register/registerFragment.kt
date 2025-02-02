package pt.ipt.dam.bookshelf.ui.auth.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentRegisterBinding
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment
import pt.ipt.dam.bookshelf.utils.ToastUtils
import java.util.regex.Pattern

/***
 * Classe responsavel por tratar do fragmento registo e receber a resposta do viewModel, para alterações de UI.
 */
class registerFragment : Fragment() {

    // Usar View Binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    companion object {
        fun newInstance() = registerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tratar a resposta da do endpoint na API (só tem uma resposta, as restantes verificações estão do lado do Android)
        viewModel.value.observe(viewLifecycleOwner, Observer { resp ->
            if(resp == "Email já está a ser utilizado"){
                ToastUtils.showCustomToast(requireContext(), "Email já está a ser utilizado!")
            }
        })

        binding.loginButton.setOnClickListener{
            //verificar se o conteúdo nos campos das passwords é diferente
            if((binding.passwordText.text.toString() != binding.confirmPasswordText.text.toString())){
                ToastUtils.showCustomToast(requireContext(), "As passwords não coincidem")
                //verificar se o regex está correto nos campos do email ou da password
            } else if (!isValidEmail(binding.textEmail.text.toString()) || !isValidPassword(binding.passwordText.text.toString()) ){
                ToastUtils.showCustomToast(requireContext(), "Confirme se a sua password ou email cumprem os requisitos necessários")
            } else {
                //se chegou aqui, é porque passou nas verificações
                viewModel.register(binding.textNome.text.toString(), binding.textApelido.text.toString(), binding.textEmail.text.toString(), binding.passwordText.text.toString())
                //mudar para o fragmento loginFragment()
                val selectedFragment = loginFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }

        }

        // clicklistener para transição para o fragment
        binding.loginLink.setOnClickListener {
            // Transição para o fragmento de Login

            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, loginFragment.newInstance())
            }
        }
    }

    //estas funções sao dedicadas ao regex do email e da password
    fun isValidEmail(email: String) : Boolean {
        return Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$").matcher(password).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpeza do binding
    }
}
