package pt.ipt.dam.bookshelf.ui.auth.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
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

        // clicklistener para transição para o fragment
        binding.loginLink.setOnClickListener {
            // Transição para o fragmento de Login
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, loginFragment.newInstance())
                addToBackStack(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpeza do binding
    }
}
