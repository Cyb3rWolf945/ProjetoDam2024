package pt.ipt.dam.bookshelf.ui.books.collections.collectionDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.databinding.ItemBookCardBinding
import pt.ipt.dam.bookshelf.models.LivrosResponse
import retrofit2.Call
import retrofit2.Response

class BooksAdapter(private var books: MutableList<LivrosResponse>) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    fun getRandomBook(): LivrosResponse? {
        return if (books.isNotEmpty()) books.random() else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding =
            ItemBookCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book, this) // Pass the adapter instance to bind()
    }

    override fun getItemCount(): Int = books.size

    class BookViewHolder(private val binding: ItemBookCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: LivrosResponse, adapter: BooksAdapter) {
            binding.titleTextView.text = book.titulo
            binding.authorTextView.text = book.autor
            binding.descriptionTextView.text = book.descricao
            binding.categoryTextView.text = book.isbn

            binding.removeButton.setOnClickListener {
                val livroId = book.idlivros
                deleteBook(livroId, adapter) // Pass the adapter instance to deleteBook
            }

            book.url.let {
                val imageUrl = it.replace("http:", "https:")
                binding.bookImageView.load(imageUrl) {
                    crossfade(true)
                    error(R.drawable.ic_launcher_background)
                }
            }
        }

        fun deleteBook(livroId: Int, adapter: BooksAdapter) {
            RetrofitClient.client.create(Service::class.java)
                .deleteBook(livroId)
                .enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            adapter.removeBookById(livroId)  // Call the method on the adapter instance
                        } else {
                            Toast.makeText(itemView.context, "Erro ao remover livro.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(itemView.context, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun removeBookById(id: Int) {
        val indexToRemove = books.indexOfFirst { it.idlivros == id }
        if (indexToRemove != -1) {
            books.removeAt(indexToRemove)  // Remove the item from the list
            notifyItemRemoved(indexToRemove)  // Notify RecyclerView to update the UI
        }
    }
}
