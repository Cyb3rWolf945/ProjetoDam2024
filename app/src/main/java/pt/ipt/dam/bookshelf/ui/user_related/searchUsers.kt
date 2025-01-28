package pt.ipt.dam.bookshelf.ui.user_related

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentRegisterBinding
import pt.ipt.dam.bookshelf.databinding.FragmentSearchUsersBinding

class searchUsers : Fragment() {

    private var _binding: FragmentSearchUsersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchUsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.value.observe(viewLifecycleOwner) { users ->

            val userNames = users.toString()
            binding.users.text = userNames
        }

        binding.searchButton.setOnClickListener{
            val value = binding.searchInputText.text.toString()
            if (value.isEmpty()){
                binding.users.text = "vazio"
            } else {
                viewModel.getUsers(value)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}