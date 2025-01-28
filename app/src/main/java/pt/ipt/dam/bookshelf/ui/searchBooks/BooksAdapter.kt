package pt.ipt.dam.bookshelf.searchBooks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pt.ipt.dam.bookshelf.R
import pt.ipt.dam.bookshelf.models.BookItem

class BooksAdapter(private var books: List<BookItem>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImageView: ImageView = itemView.findViewById(R.id.bookImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorsTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(book: BookItem) {
            val volumeInfo = book.volumeInfo

            // Set the book title, authors, and description
            titleTextView.text = volumeInfo?.title ?: "No Title"
            authorsTextView.text = volumeInfo?.authors?.joinToString(", ") ?: "No Authors"
            descriptionTextView.text = volumeInfo?.description ?: "No Description Available"

            volumeInfo?.imageLinks?.thumbnail?.let {
                val imageUrl = it.replace("http:", "https:")
                bookImageView.load(imageUrl) {
                    crossfade(true)
                    error(R.drawable.ic_launcher_background)
                }
            }

            // Set a click listener on the entire itemView
            itemView.setOnClickListener {
                val isbn = volumeInfo?.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier
                isbn?.let { onItemClick(it) } // Pass ISBN to the click handler
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_card, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<BookItem>) {
        books = newBooks
        notifyDataSetChanged()
    }
}

