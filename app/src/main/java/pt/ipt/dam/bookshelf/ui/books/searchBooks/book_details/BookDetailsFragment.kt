package pt.ipt.dam.bookshelf.ui.books.searchBooks.book_details

import UserPreferences.getUser
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import pt.ipt.dam.bookshelf.MainActivity
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.databinding.FragmentBookDetailsBinding
import pt.ipt.dam.bookshelf.models.BookItem
import pt.ipt.dam.bookshelf.models.Livros
import pt.ipt.dam.bookshelf.searchBooks.search_books
import pt.ipt.dam.bookshelf.ui.books.collections.Collections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailsFragment : Fragment() {

    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookDetailsViewModel by viewModels()

    private lateinit var isbn: String
    private lateinit var imageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)

        // Retrieve ISBN from arguments
        arguments?.let {
            isbn = it.getString("ISBN") ?: ""
        }

        binding.backButton.setOnClickListener {
            onClose()
        }

        binding.addButton.setOnClickListener {
            onAddBook()
        }

        // Observe the ViewModel
        setupObservers()

        // Fetch book details
        viewModel.fetchBookDetails(isbn, "AIzaSyBJaKQAJgsSeHOOdY0uZVhufLBDZN-Ps7I")

        return binding.root
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BookDetailsState.Loading -> {
                    showLoading()
                }
                is BookDetailsState.Success -> {
                    showBookDetails(state.book)
                }
                is BookDetailsState.Error -> {
                    showError(state.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.bookDetailsLayout.visibility = View.GONE
    }

    private fun showBookDetails(book: BookItem) {
        binding.progressBar.visibility = View.GONE
        binding.bookDetailsLayout.visibility = View.VISIBLE

        binding.titleTextView.text = book.volumeInfo?.title ?: "N/A"
        binding.authorsTextView.text = book.volumeInfo?.authors?.joinToString(", ") ?: "N/A"
        binding.descriptionTextView.text = book.volumeInfo?.description ?: "N/A"
        binding.publishedDateTextView.text = book.volumeInfo?.publishedDate ?: "N/A"
        binding.pagesTextView.text = book.volumeInfo?.pageCount?.toString() ?: "N/A"
        binding.averageRatingTextView.text = book.volumeInfo?.averageRating?.toString() ?: "N/A"

        book.volumeInfo?.imageLinks?.thumbnail?.let {
            imageUrl = it.replace("http:", "https:")
            binding.coverImageView.load(imageUrl) {
                crossfade(true)
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.bookDetailsLayout.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun onClose(){
        val selectedFragment = search_books()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .addToBackStack(null)
            .commit()
    }

    fun onAddBook(){
        val retrofit = RetrofitClient.client
        val bookApi = retrofit.create(Service::class.java)

        val user = getUser()
        val userId = user?.first ?: -1

        // Criar objeto Livros compatível com o backend
        val livro = Livros(
            nome = binding.titleTextView.text.toString(),
            dataemissao = binding.publishedDateTextView.text.toString(),
            autor = binding.authorsTextView.text.toString(),
            descricao = binding.descriptionTextView.text.toString(),
            rating = binding.averageRatingTextView.text.toString().toFloatOrNull() ?: 0f,
            ISBN = isbn,
            paginas = binding.pagesTextView.text.toString().toIntOrNull() ?: 0,
            url = imageUrl
        )

        // Verificar se o userId é válido
        if (userId == -1) {
            Log.e("AddBook", "Invalid user ID")
            return
        }

        // Chamar a API corretamente
        val call = bookApi.addBook(userId, livro)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("AddBook", "Book added successfully!")
                    val selectedFragment = Collections()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null)
                        .commit()
                    (activity as? MainActivity)?.setBottomNavigationItem(R.id.item_1)
                } else {
                    Log.e("Error", "Failed to add book: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AddBook", "Error adding book: ${t.message}")
            }
        })
    }
}
