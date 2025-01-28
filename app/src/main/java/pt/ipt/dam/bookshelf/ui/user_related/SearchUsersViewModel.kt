package pt.ipt.dam.bookshelf.ui.user_related

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.Utilizadores
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUsersViewModel : ViewModel() {
    private val _users = MutableLiveData<String>()
    val users: LiveData<String> get() = _users

    private val api = RetrofitClient.client.create(Service::class.java)

    fun searchUsers(email: String) {
        val call = api.getUsers(email)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.v("testeresponse", response.body().toString())
                    _users.value = response.body()
                } else {
                    _users.value = ""
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.v("testeresponse", "erro", t)
                _users.value = ""
            }
        })
    }
}