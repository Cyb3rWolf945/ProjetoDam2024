package pt.ipt.dam.bookshelf.ui.auth.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.LoginResponse
import pt.ipt.dam.bookshelf.models.UserLogin
import pt.ipt.dam.bookshelf.models.Utilizadores
import pt.ipt.dam.bookshelf.utils.ToastUtils
import pt.ipt.dam.bookshelf.utils.UserCacheUtil
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _login = MutableLiveData<LoginResponse?>()
    val login: LiveData<LoginResponse?> get() = _login

    private val api = RetrofitClient.client.create(Service::class.java)

    init {
        UserPreferences.init(application)
    }

    fun login(email: String, password: String) {
        val request = Utilizadores(
            userid = 0,
            email = email,
            password = password,
            nome = "",
            apelido = "",
        )

        api.login(request).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        _login.value = resp
                        UserPreferences.saveUser(resp.userid, resp.nome)

                    }
                    Log.v("teste", response.body().toString())
                } else {
                    _login.value = LoginResponse(userid = 0, nome = "Erro ao fazer login")
                    ToastUtils.showCustomToast(getApplication(), "Erro ao fazer login")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _login.value = LoginResponse(userid = 0, nome = "Erro de conexão")
                Log.v("teste", "Falha na requisição", t)
            }
        })
    }
}