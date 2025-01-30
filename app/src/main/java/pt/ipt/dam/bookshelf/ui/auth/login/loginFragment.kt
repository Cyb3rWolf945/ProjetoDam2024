package pt.ipt.dam.bookshelf.ui.auth.login

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentLoginBinding
import pt.ipt.dam.bookshelf.ui.auth.register.registerFragment
import pt.ipt.dam.bookshelf.utils.ToastUtils
import java.util.regex.Pattern

class loginFragment : Fragment() {

    // Usar View Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun newInstance() = loginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                ToastUtils.showCustomToast(requireContext(), "Preencha os campos necessários")
            } else {
                viewModel.login(email, password)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.login.observe(viewLifecycleOwner, Observer { loginResponse ->
            if (loginResponse != null && loginResponse.userid != 0 && loginResponse.nome.isNotEmpty()) {
                ToastUtils.showCustomToast(requireContext(), "Login efetuado com sucesso")
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("userId", loginResponse.userid)
                intent.putExtra("userName", loginResponse.nome)
                startActivity(intent)
                activity?.finish()
            } else {

                ToastUtils.showCustomToast(requireContext(), "Campos incorretos")
            }
        })

        // Transição para o fragmento de registo
        binding.registerLink.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, registerFragment.newInstance())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
