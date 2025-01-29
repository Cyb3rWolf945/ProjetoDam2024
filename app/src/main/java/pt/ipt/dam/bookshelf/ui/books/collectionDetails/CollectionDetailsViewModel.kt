package pt.ipt.dam.bookshelf.ui.books.collectionDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.Livros
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionDetailsViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Livros>>()
    val books: LiveData<List<Livros>> get() = _books

    private val service = RetrofitClient.client.create(Service::class.java)

    fun fetchBooksForCollection(userId: Int, collectionId: Int) {
        service.getBooksForCollection(userId, collectionId).enqueue(object : Callback<List<Livros>> {
            override fun onResponse(call: Call<List<Livros>>, response: Response<List<Livros>>) {
                if (response.isSuccessful) {
                    _books.value = response.body()
                } else {
                    // Handle error response here
                    //Log.e("CollectionDetailsViewModel", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Livros>>, t: Throwable) {
                // Handle failure here
                //Log.e("CollectionDetailsViewModel", "Failure: ${t.message}")
            }
        })
    }
}