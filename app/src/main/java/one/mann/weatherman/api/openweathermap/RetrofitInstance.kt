package one.mann.weatherman.api.openweathermap

import okhttp3.OkHttpClient
import one.mann.weatherman.api.Keys
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitInstance {
    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    private const val QUERY_APPID = "appid"
    private const val QUERY_UNITS = "units"
    private const val UNITS = "metric"
    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(QueryInterceptor(QUERY_APPID, Keys.OWM_APP_ID))
            .addInterceptor(QueryInterceptor(QUERY_UNITS, UNITS))
            .build()
    val service: OwmService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .run { create(OwmService::class.java) }
}