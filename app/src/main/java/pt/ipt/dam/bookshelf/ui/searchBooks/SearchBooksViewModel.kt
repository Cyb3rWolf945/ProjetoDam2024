package pt.ipt.dam.bookshelf.ui.searchBooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipt.dam.bookshelf.Services.GoogleBooksRetrofitClient
import pt.ipt.dam.bookshelf.models.BookItem

class SearchBooksViewModel : ViewModel() {

    // LiveData to observe the list of books
    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> get() = _books

    // Function to search books based on a query
    fun searchBooks(query: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch the book info from the API using the Retrofit client
                val response = GoogleBooksRetrofitClient.googleBooksApi.fetchBookInfo(query, apiKey).execute()
                if (response.isSuccessful) {
                    // Post the list of books or an empty list if no books found
                    _books.postValue(response.body()?.items ?: emptyList())
                } else {
                    _books.postValue(emptyList())
                }
            } catch (e: Exception) {
                // In case of error, post an empty list
                _books.postValue(emptyList())
            }
        }
    }
}