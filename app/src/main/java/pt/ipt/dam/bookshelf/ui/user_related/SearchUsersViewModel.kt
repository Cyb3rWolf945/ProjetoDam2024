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

/***
 * ViewModel responsavél pela pesquisa de utilizadores da API.
 * Em caso de sucesso: devolve um utilizador para ser inserido no cartão.
 * Em caso de erro: não devolve nada para ser usado no fragmento.
 */
class SearchUsersViewModel : ViewModel() {
    private val _users = MutableLiveData<Utilizadores>()
    val users: LiveData<Utilizadores> get() = _users

    private val api = RetrofitClient.client.create(Service::class.java)

    fun searchUsers(email: String) {
        val call = api.getUsers(email)

        call.enqueue(object : Callback<Utilizadores> {
            override fun onResponse(call: Call<Utilizadores>, response: Response<Utilizadores>) {
                if (response.isSuccessful) {
                    Log.v("testeresponse", response.body().toString())
                    _users.value = response.body()
                } else {
                    _users.value = Utilizadores(
                        userid = 0,
                        nome = "",
                        apelido = "",
                        email = "",
                        password = ""
                    )
                }
            }

            override fun onFailure(call: Call<Utilizadores>, t: Throwable) {
                Log.v("testeresponse", "erro", t)
                _users.value = Utilizadores(
                    userid = 0,
                    nome = "",
                    apelido = "",
                    email = "",
                    password = ""
                )
            }
        })
    }
}