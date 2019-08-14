package one.mann.weatherman.di.module.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import one.mann.weatherman.api.common.Keys
import one.mann.weatherman.api.common.QueryInterceptor
import one.mann.weatherman.api.openweathermap.OwmService
import one.mann.weatherman.api.teleport.TeleportService
import one.mann.weatherman.di.annotations.qualifier.AppId
import one.mann.weatherman.di.annotations.qualifier.OpenWeatherMap
import one.mann.weatherman.di.annotations.qualifier.Teleport
import one.mann.weatherman.di.annotations.qualifier.Units
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
internal class ApiServiceModule {

    companion object {
        private const val TELEPORT_BASE_URL = "https://api.teleport.org/api/locations/"
        private const val OWM_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val QUERY_APPID = "appid"
        private const val QUERY_UNITS = "units"
        private const val DEFAULT_UNITS = "metric"
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @AppId
    fun provideAppIdQueryInterceptor(): QueryInterceptor = QueryInterceptor(QUERY_APPID, Keys.OWM_APPID)

    @Provides
    @Singleton
    @Units
    fun provideUnitsQueryInterceptor(): QueryInterceptor = QueryInterceptor(QUERY_UNITS, DEFAULT_UNITS)

    @Provides
    @Singleton
    fun provideOkHttpClient(@AppId appIdQueryInterceptor: QueryInterceptor, @Units unitsQueryInterceptor: QueryInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(appIdQueryInterceptor)
            .addInterceptor(unitsQueryInterceptor)
            .build()

    @Provides
    @Singleton
    @OpenWeatherMap
    fun provideOwmRestAdapter(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit =
            Retrofit.Builder()
                    .baseUrl(OWM_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .client(client)
                    .build()

    @Provides
    @Singleton
    @Teleport
    fun provideTeleportRestAdapter(gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
            .baseUrl(TELEPORT_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideOwmService(@OpenWeatherMap retrofit: Retrofit): OwmService = retrofit.create(OwmService::class.java)

    @Provides
    @Singleton
    fun provideTeleportService(@Teleport retrofit: Retrofit): TeleportService = retrofit.create(TeleportService::class.java)
}