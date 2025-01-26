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

            if(email.equals("") and password.equals("")){
                Toast.makeText(requireContext(), "Preencha os campos necessários", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.login.observe(viewLifecycleOwner, Observer { resp ->
            if (resp == "Success"){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(requireContext(), "Campos incorretos", Toast.LENGTH_SHORT).show()
            }
        })

        // clicklistener para transição para o fragment
        binding.registerLink.setOnClickListener {
            // Transição para o fragmento de registo
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, registerFragment.newInstance())
                addToBackStack(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
