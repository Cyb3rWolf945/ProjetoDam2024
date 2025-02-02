package pt.ipt.dam.bookshelf.Services

import pt.ipt.dam.bookshelf.models.GoogleBooksResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/***
 * Interface para fetch dos livros da Google API
 * PARAMS:
 * ISBN OU NOME
 * APIKEY
 */
interface GoogleBooksApi{
    @GET("volumes")
    fun fetchBookInfo(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Call<GoogleBooksResponse>
}