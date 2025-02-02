package pt.ipt.dam.bookshelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.GoogleBooksRetrofitClient
import pt.ipt.dam.bookshelf.models.GoogleBooksResponse
import pt.ipt.dam.bookshelf.models.VolumeInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Esta classe ViewModel é responsavél por ir buscar informações do livro digitalizado
 * quando é utilizado o hardware.
 */
class BookInfoViewModel : ViewModel() {

    private val _bookInfoState = MutableLiveData<BookInfoState>()
    val bookInfoState: LiveData<BookInfoState> get() = _bookInfoState

    /***
     * Esta função encarrega-se portanto de realizar o fetch a google API BOOKS
     */
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

/***
 * Classe usada apenas neste ficheiro. Pois uma classe sealed só pode ser extendida por outra classe no mesmo ficheiro.
 * Esta reponsavél por definir o estado do pedido em Loading por defeito, sucesso ou erro.
 */
sealed class BookInfoState {
    object Loading : BookInfoState()
    data class Success(val bookInfo: VolumeInfo) : BookInfoState()
    data class Error(val message: String) : BookInfoState()
}
