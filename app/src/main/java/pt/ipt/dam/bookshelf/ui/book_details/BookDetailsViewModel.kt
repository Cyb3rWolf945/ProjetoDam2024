package pt.ipt.dam.bookshelf.ui.book_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipt.dam.bookshelf.Services.GoogleBooksRetrofitClient
import pt.ipt.dam.bookshelf.models.BookItem

sealed class BookDetailsState {
    object Loading : BookDetailsState()
    data class Success(val book: BookItem) : BookDetailsState()
    data class Error(val message: String) : BookDetailsState()
}

class BookDetailsViewModel : ViewModel() {
    private val _state = MutableLiveData<BookDetailsState>()
    val state: LiveData<BookDetailsState> get() = _state

    fun fetchBookDetails(isbn: String, apiKey: String) {
        _state.value = BookDetailsState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = GoogleBooksRetrofitClient.googleBooksApi.fetchBookInfo("isbn:$isbn", apiKey).execute()
                if (response.isSuccessful && response.body()?.items?.isNotEmpty() == true) {
                    _state.postValue(BookDetailsState.Success(response.body()!!.items!![0]))
                } else {
                    _state.postValue(BookDetailsState.Error("Book not found"))
                }
            } catch (e: Exception) {
                _state.postValue(BookDetailsState.Error("Error fetching book details: ${e.message}"))
            }
        }
    }
}