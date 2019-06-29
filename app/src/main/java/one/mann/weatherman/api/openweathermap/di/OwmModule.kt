package one.mann.weatherman.api.openweathermap.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import one.mann.weatherman.api.common.Keys
import one.mann.weatherman.api.openweathermap.OwmService
import one.mann.weatherman.api.openweathermap.QueryInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
internal class OwmModule {

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private const val QUERY_APPID = "appid"
        private const val QUERY_UNITS = "units"
        private const val UNITS = "metric"
    }

    @Provides
//    @Singleton
    @Named("AppIdQuery")
    fun provideAppIdQueryInterceptor(): QueryInterceptor = QueryInterceptor(QUERY_APPID, Keys.OWM_APPID)

    @Provides
//    @Singleton
    @Named("UnitsQuery")
    fun provideUnitsQueryInterceptor(): QueryInterceptor = QueryInterceptor(QUERY_UNITS, UNITS)

    @Provides
//    @Singleton
    fun provideOkHttpClient(@Named("AppIdQuery") appIdQueryInterceptor: QueryInterceptor,
                            @Named("UnitsQuery") unitsQueryInterceptor: QueryInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(appIdQueryInterceptor)
            .addInterceptor(unitsQueryInterceptor)
            .build()

    @Provides
//    @Singleton
    @Named("OwmInstance")
    fun provideOwmRestAdapter(okHttpClient: OkHttpClient,
                              gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()

    @Provides
//    @Singleton
    fun provideOwmService(@Named("OwmInstance") retrofit: Retrofit): OwmService =
            retrofit.create(OwmService::class.java)
}