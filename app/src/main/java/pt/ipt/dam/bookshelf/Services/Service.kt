package pt.ipt.dam.bookshelf.Services
import pt.ipt.dam.bookshelf.models.CollectionsResponse
import pt.ipt.dam.bookshelf.models.Livros
import pt.ipt.dam.bookshelf.models.LivrosResponse
import pt.ipt.dam.bookshelf.models.LoginResponse
import pt.ipt.dam.bookshelf.models.Utilizadores
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface Service {
    @POST("/login")
    fun login(@Body utilizador: Utilizadores): Call<LoginResponse>

    @PUT("/users/update")
    fun updateUser(@Body user: Utilizadores): Call<Void>

    @DELETE("/users/delete")
    fun deleteUser(@Query("userid") userid: Int): Call<Void>


    @POST("/users/")
    fun register(@Body utilizador: Utilizadores): Call<String>

    @POST("/livros/")
    fun addBook(@Body livro: Livros): Call<Void>

    @GET("colecoes")
    fun getCollections(@Query("userid") userid: Int): Call<CollectionsResponse>
    @GET("/users/")

    fun getUsers(@Query ("email") email: String):Call<Utilizadores>

    @GET("/users/{user_id}/collections/{collection_id}/books")
    fun getBooksForCollection(
        @Path("user_id") userId: Int,
        @Path("collection_id") collectionId: Int
    ): Call<Map<String, List<LivrosResponse>>>

    @POST("users/{userId}/livros")
    fun addBook(
        @Path("userId") userId: Int,
        @Body livro: Livros
    ): Call<Void>

}