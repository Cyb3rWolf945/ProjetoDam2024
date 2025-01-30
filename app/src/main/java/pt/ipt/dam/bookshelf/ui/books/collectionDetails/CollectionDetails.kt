package pt.ipt.dam.bookshelf.ui.books.collectionDetails

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentCollectionDetailsBinding

class CollectionDetailsFragment : Fragment() {

    private val viewModel: CollectionDetailsViewModel by viewModels()
    private var _binding: FragmentCollectionDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var booksAdapter: BooksAdapter

    private var collectionId: Int = -1
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recebe o ID da coleção e do utilizador
        arguments?.let {
            collectionId = it.getInt("idcolecoes")
            userId = it.getInt("userId")
        }

        // Verifica se o collectionId é válido
        if (collectionId == -1 || userId == -1) {
            Log.e("CollectionDetails", "Collection ID or User ID is invalid!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Buscar os livros da coleção
        viewModel.fetchBooksForCollection(userId, collectionId)

        // Observar mudanças nos livros
        viewModel.books.observe(viewLifecycleOwner, Observer { books ->
            if (books != null) {
                booksAdapter = BooksAdapter(books)
                binding.recyclerViewBooks.adapter = booksAdapter
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerViewBooks.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}