package pt.ipt.dam.bookshelf.searchBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipt.dam.bookshelf.BuildConfig
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentSearchBooksBinding
import pt.ipt.dam.bookshelf.ui.books.searchBooks.book_details.BookDetailsFragment
import pt.ipt.dam.bookshelf.ui.books.searchBooks.SearchBooksViewModel

class search_books : Fragment() {

    companion object {
        fun newInstance() = search_books()
    }

    private val viewModel: SearchBooksViewModel by viewModels()

    private var _binding: FragmentSearchBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with BooksAdapter
        val adapter = BooksAdapter(emptyList()) { isbn ->
            // Pass the ISBN to the book details fragment
            val bundle = Bundle().apply {
                putString("ISBN", isbn)
            }
            val bookDetailsFragment = BookDetailsFragment().apply {
                arguments = bundle
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, bookDetailsFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.searchBooksRecycler.adapter = adapter
        binding.searchBooksRecycler.layoutManager = LinearLayoutManager(context)

        // Observe the LiveData from the ViewModel
        viewModel.books.observe(viewLifecycleOwner) { books ->
            adapter.updateBooks(books)
        }

        // Handle search button click
        binding.searchButton.setOnClickListener {
            val query = binding.searchInputText.text.toString().trim()
            if (query.isNotBlank()) {
                viewModel.searchBooks(query, BuildConfig.BOOKS_API_KEY)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

