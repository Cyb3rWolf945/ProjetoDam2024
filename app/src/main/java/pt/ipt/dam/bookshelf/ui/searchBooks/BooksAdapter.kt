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

class BooksAdapter(private var books: List<BookItem>) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    // ViewHolder class that binds the book data to the UI components
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


            volumeInfo?.imageLinks?.thumbnail?.replace("http:", "https:")

            // Set the image using Coil
            volumeInfo?.imageLinks?.thumbnail?.let {
                val imageUrl = it.replace("http:", "https:")
                bookImageView.load(imageUrl) {
                    crossfade(true) // Adds smooth transition for image loading
                    error(R.drawable.ic_launcher_background) // Default error image
                }
            }
        }
    }

    // Create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_card, parent, false)
        return BookViewHolder(view)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int = books.size

    // Update the list of books in the adapter
    fun updateBooks(newBooks: List<BookItem>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
