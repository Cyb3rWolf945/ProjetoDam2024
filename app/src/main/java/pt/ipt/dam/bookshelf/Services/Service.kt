package pt.ipt.dam.bookshelf.Services
import pt.ipt.dam.bookshelf.models.CollectionsResponse
import pt.ipt.dam.bookshelf.models.Livros
import pt.ipt.dam.bookshelf.models.LoginResponse
import pt.ipt.dam.bookshelf.models.Utilizadores
import pt.ipt.dam.bookshelf.models.VolumeInfo
import pt.ipt.dam.bookshelf.models.collection
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query


interface Service {
    @POST("/login")
    fun login(@Body utilizador: Utilizadores): Call<LoginResponse>

    @PUT("/users/update")
    fun updateUser(@Body user: Utilizadores): Call<Void>

    @DELETE("/users/delete")
    fun deleteUser(@Body user: Utilizadores): Call<Void>


    @POST("/users/")
    fun register(@Body utilizador: Utilizadores): Call<String>

    @POST("/livros/")
    fun addBook(@Body livro: Livros): Call<Void>

    @GET("colecoes")
    fun getCollections(@Query("userid") userid: Int): Call<CollectionsResponse>
    @GET("/users/")

    fun getUsers(@Query ("email") email: String):Call<Utilizadores>

}