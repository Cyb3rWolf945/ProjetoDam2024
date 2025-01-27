package pt.ipt.dam.bookshelf.ui.auth.register

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

class RegisterViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _value = MutableLiveData<String>()
    val value: LiveData<String> get() = _value

    private val api = RetrofitClient.client.create(Service::class.java)

    fun register(nome: String, apelido: String, email:String, password: String) {
        val request = Utilizadores(
            nome = nome,
            apelido = apelido,
            email = email,
            password = password,
            userid = 0 //aqui não interessa muito o que se passa porque tem autoincrement na tabela Utilizadores
        )

        api.register(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _value.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _value.value = "não"
            }

        })
    }

}