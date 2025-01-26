package pt.ipt.dam.bookshelf.ui.auth.register

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentRegisterBinding
import pt.ipt.dam.bookshelf.ui.auth.login.loginFragment

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

        viewModel.value.observe(viewLifecycleOwner, Observer { resp ->
            if(binding.passwordText.text.toString() != binding.confirmPasswordText.text.toString()){
                Toast.makeText(requireContext(), "As passwords não coincidem", Toast.LENGTH_SHORT).show()

                //_binding = null

            } else if(resp == "Email já está a ser utilizado"){
                Toast.makeText(requireContext(), "Email já está a ser utilizado", Toast.LENGTH_SHORT).show()
                //_binding = null
            } else {
                val selectedFragment = loginFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }
        })

        binding.loginButton.setOnClickListener{
            viewModel.register(binding.textNome.text.toString(), binding.textApelido.text.toString(), binding.textEmail.text.toString(), binding.passwordText.text.toString())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpeza do binding
    }
}
