package pt.ipt.dam.bookshelf.ui.auth.register

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

    fun register(email: String, nome: String, apelido: String, password: String) {
        val request = Utilizadores(
            email = email,
            nome = nome,
            apelido = apelido,
            password = password,
            userid = 0
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