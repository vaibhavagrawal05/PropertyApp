package eu.tutorials.propertyapp

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://yourapiurl.com/"

    val api: PropertyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PropertyApiService::class.java)
    }
}

interface PropertyApiService {
    @GET("properties")
    suspend fun getProperties(@Header("Authorization") apiKey: String): List<Property>
}