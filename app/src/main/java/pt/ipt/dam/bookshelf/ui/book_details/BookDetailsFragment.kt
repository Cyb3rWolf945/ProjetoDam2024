package pt.ipt.dam.bookshelf.ui.book_details

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.FragmentBookDetailsBinding
import pt.ipt.dam.bookshelf.models.BookItem

class BookDetailsFragment : Fragment() {

    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookDetailsViewModel by viewModels()

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
            val imageUrl = it.replace("http:", "https:")
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
}
