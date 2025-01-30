package pt.ipt.dam.bookshelf.ui.books.collectionDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.databinding.ItemBookCardBinding
import pt.ipt.dam.bookshelf.models.Livros

class BooksAdapter(private val books: List<Livros>) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = books.size

    class BookViewHolder(private val binding: ItemBookCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Livros) {
            binding.titleTextView.text = book.nome
            binding.authorTextView.text = book.autor
            binding.descriptionTextView.text = book.descricao
            binding.categoryTextView.text = book.ISBN

            book.url.let {
                val imageUrl = it.replace("http:", "https:")
                binding.bookImageView.load(imageUrl) {
                    crossfade(true)
                    error(R.drawable.ic_launcher_background)
                }
            }
        }
    }
}