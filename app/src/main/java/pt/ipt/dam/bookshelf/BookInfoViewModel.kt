package pt.ipt.dam.bookshelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipt.dam.bookshelf.Services.GoogleBooksRetrofitClient
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.models.GoogleBooksResponse
import pt.ipt.dam.bookshelf.models.VolumeInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookInfoViewModel : ViewModel() {

    private val _bookInfoState = MutableLiveData<BookInfoState>()
    val bookInfoState: LiveData<BookInfoState> get() = _bookInfoState

    fun fetchBookInfo(isbn: String, apiKey: String) {
        _bookInfoState.value = BookInfoState.Loading

        GoogleBooksRetrofitClient.googleBooksApi.fetchBookInfo(isbn, apiKey).enqueue(object :
            Callback<GoogleBooksResponse> {
            override fun onResponse(call: Call<GoogleBooksResponse>, response: Response<GoogleBooksResponse>) {
                if (response.isSuccessful) {
                    val bookInfo = response.body()?.items?.firstOrNull()?.volumeInfo
                    if (bookInfo != null) {
                        _bookInfoState.value = BookInfoState.Success(bookInfo)
                    } else {
                        _bookInfoState.value = BookInfoState.Error("No book information found")
                    }
                } else {
                    _bookInfoState.value = BookInfoState.Error("Error fetching book information")
                }
            }

            override fun onFailure(call: Call<GoogleBooksResponse>, t: Throwable) {
                _bookInfoState.value = BookInfoState.Error(t.message ?: "Unknown error")
            }
        })
    }
}

sealed class BookInfoState {
    object Loading : BookInfoState()
    data class Success(val bookInfo: VolumeInfo) : BookInfoState()
    data class Error(val message: String) : BookInfoState()
}
