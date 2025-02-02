package pt.ipt.dam.bookshelf.ui.books.collections

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.CollectionsResponse
import pt.ipt.dam.bookshelf.models.collection
import pt.ipt.dam.bookshelf.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Esta classe vai ser responsavél por tratar da logica de pedidos a API.
 * Usa variavel LiveData para armazenar o resultado da resposta coleções em caso de sucesso ou falha.
 */
class CollectionsViewModel : ViewModel() {
    private val _collections = MutableLiveData<List<collection>>()
    val books: LiveData<List<collection>> get() = _collections

    private val retrofit = RetrofitClient.client
    private val service = retrofit.create(Service::class.java)

    fun fetchCollections(userid: Int) {
        val call = service.getCollections(userid)
        call.enqueue(object : Callback<CollectionsResponse> {
            override fun onResponse(call: Call<CollectionsResponse>, response: Response<CollectionsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { collectionsResponse ->
                        _collections.postValue(collectionsResponse.colecoes)
                        Log.d("Collections", "Received ${collectionsResponse.colecoes.size} collections")
                    }
                } else {
                    Log.e("Error", "Failed to fetch collections: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CollectionsResponse>, t: Throwable) {
                Log.e("Error", "Error fetching collections: ${t.message}")
            }
        })
    }
}