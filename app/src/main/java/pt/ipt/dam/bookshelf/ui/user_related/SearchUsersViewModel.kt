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

    private val _value = MutableLiveData<String>()
    val value: LiveData<String> get() = _value

    private val api = RetrofitClient.client.create(Service::class.java)

    fun getUsers(email:String){

        api.getUsers(email).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>,
            ) {
                if (response.isSuccessful){
                    Log.v("success", "deu certo")
                    _value.value = response.body()
                } else {
                    Log.v("not so good", "deu certo")
                    _value.value = ""
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("not so good", "deu certo", t)
                _value.value = "não"
            }


        })
    }
}