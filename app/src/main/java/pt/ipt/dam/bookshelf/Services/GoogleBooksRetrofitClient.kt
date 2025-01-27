package pt.ipt.dam.bookshelf.Services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleBooksRetrofitClient {

    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val googleBooksApi: GoogleBooksApi = retrofit.create(GoogleBooksApi::class.java)
}