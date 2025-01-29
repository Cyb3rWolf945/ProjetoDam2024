package pt.ipt.dam.bookshelf.ui.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ipt.dam.bookshelf.Services.RetrofitClient
import pt.ipt.dam.bookshelf.Services.Service
import pt.ipt.dam.bookshelf.models.Utilizadores
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val api = RetrofitClient.client.create(Service::class.java)
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getUserId(): Int {
        return sharedPreferences.getInt("userid", -1)
    }

    fun updateUser(newName: String, newEmail: String, newPassword: String, apelido: String) {
        val userid = getUserId()
        if (userid == -1) {
            _updateSuccess.value = false
            return
        }

        val request = Utilizadores(
            userid = userid,
            email = newEmail,
            password = newPassword,
            nome = newName,
            apelido = apelido
        )

        api.updateUser(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _updateSuccess.value = response.isSuccessful
                Log.v("teste", "deu certo " + response.body().toString())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _updateSuccess.value = false
                Log.v("teste", "deu errado", t)
            }
        })
    }

    fun deleteUser(userId: Int) {
        if (userId == -1) {
            _updateSuccess.value = false
            return
        }

        api.deleteUser(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.v("teste", "deu certo")
                    sharedPreferences.edit().remove("userid").apply()

                    _updateSuccess.value = true
                } else {
                    Log.v("teste", "errado")
                    _updateSuccess.value = false
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _updateSuccess.value = false
                Log.v("teste", "erro", t)
            }
        })
    }
}