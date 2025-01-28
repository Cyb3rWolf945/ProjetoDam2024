package pt.ipt.dam.bookshelf.ui.books.collections

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.collection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionsViewModel : ViewModel() {

    private val _collections = MutableLiveData<List<collection>>()
    val collections: LiveData<List<collection>> get() = _collections

    private val retrofit = RetrofitClient.client  // Using your RetrofitClient
    private val service = retrofit.create(Service::class.java)

    // Function to fetch collections from the API
    fun fetchCollections(userid: Int) {
        val call = service.getCollections(userid)
        call.enqueue(object : Callback<List<collection>> {
            override fun onResponse(call: Call<List<collection>>, response: Response<List<collection>>) {
                if (response.isSuccessful) {
                    _collections.value = response.body() // Set the fetched collections
                } else {
                    Log.e("Error", "Failed to fetch collections")
                }
            }

            override fun onFailure(call: Call<List<collection>>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
            }
        })
    }
}