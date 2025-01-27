package pt.ipt.dam.bookshelf.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.Utilizadores
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _login = MutableLiveData<String>()
    val login: LiveData<String?> get() = _login //encapsulamento

    private val api = RetrofitClient.client.create(Service::class.java) //instância do retrofit para a API com a implementacao da interface

    //para não ter que fazer um model novo apenas com dois parametros, criei um objeto e passei valores por defeito
    fun login(email: String, password: String){
        val request = Utilizadores(
            userid = 0,
            email = email,
            password = password,
            nome = "",
            apelido = "",
        )

        api.login(request).enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    _login.value = response.body()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                _login.value = "não"
            }

        })
    }
}