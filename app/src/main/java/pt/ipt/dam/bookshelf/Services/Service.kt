package pt.ipt.dam.bookshelf.Services
import pt.ipt.dam.bookshelf.models.Utilizadores
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface Service {
    @POST("/login")
    fun login(@Body utilizador: Utilizadores): Call<String>

    @POST("/users/")
    fun register(@Body utilizador: Utilizadores): Call<String>
}