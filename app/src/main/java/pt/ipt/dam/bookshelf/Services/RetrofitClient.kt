package pt.ipt.dam.bookshelf.Services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//1 - class retrofit
object RetrofitClient {
    private var retrofit: Retrofit? = null
    val client: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
}
