package one.mann.weatherman.api.openweathermap

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitInstance {
    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    val service: OwmService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .run { create(OwmService::class.java) }
}