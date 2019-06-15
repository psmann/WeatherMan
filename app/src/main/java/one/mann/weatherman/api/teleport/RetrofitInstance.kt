package one.mann.weatherman.api.teleport

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitInstance {
    private const val BASE_URL = "https://api.teleport.org/api/locations/"
    val service: TeleportService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .run { create<TeleportService>(TeleportService::class.java) }
}