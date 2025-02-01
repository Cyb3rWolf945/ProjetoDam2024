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
                    .baseUrl("https://apidam-ftfvhaf7djdvd5fr.canadacentral-01.azurewebsites.net")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
}
