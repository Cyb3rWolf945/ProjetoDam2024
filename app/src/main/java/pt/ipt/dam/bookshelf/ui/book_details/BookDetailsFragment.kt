package pt.ipt.dam.bookshelf.ui.book_details

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentBookDetailsBinding

class BookDetailsFragment : Fragment() {

    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var isbn: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)

        // Retrieve ISBN from arguments
        arguments?.let {
            isbn = it.getString("ISBN") ?: ""
        }

        // Use the ISBN to fetch book details from the ViewModel or API here
        binding.bookISBN.text = isbn
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
