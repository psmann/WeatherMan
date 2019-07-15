package one.mann.weatherman.application.di.module.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import one.mann.weatherman.api.common.Keys
import one.mann.weatherman.api.openweathermap.OwmService
import one.mann.weatherman.api.openweathermap.QueryInterceptor
import one.mann.weatherman.api.teleport.TeleportService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class ApiServiceModule {

    companion object {
        private const val TELEPORT_BASE_URL = "https://api.teleport.org/api/locations/"
        private const val OWM_BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private const val QUERY_APPID = "appid"
        private const val QUERY_UNITS = "units"
        private const val DEFAULT_UNITS = "metric"
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @Named("AppIdQuery")
    fun provideAppIdQueryInterceptor(): QueryInterceptor = QueryInterceptor(QUERY_APPID, Keys.OWM_APPID)

    @Provides
    @Singleton
    @Named("UnitsQuery")
    fun provideUnitsQueryInterceptor(): QueryInterceptor = QueryInterceptor(QUERY_UNITS, DEFAULT_UNITS)

    @Provides
    @Singleton
    fun provideOkHttpClient(
            @Named("AppIdQuery") appIdQueryInterceptor: QueryInterceptor,
            @Named("UnitsQuery") unitsQueryInterceptor: QueryInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(appIdQueryInterceptor)
            .addInterceptor(unitsQueryInterceptor)
            .build()

    @Provides
    @Singleton
    @Named("OwmInstance")
    fun provideOwmRestAdapter(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit =
            Retrofit.Builder()
                    .baseUrl(OWM_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .client(client)
                    .build()

    @Provides
    @Singleton
    @Named("TeleportInstance")
    fun provideTeleportRestAdapter(gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
            .baseUrl(TELEPORT_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideOwmService(@Named("OwmInstance") retrofit: Retrofit): OwmService =
            retrofit.create(OwmService::class.java)

    @Provides
    @Singleton
    fun provideTeleportService(@Named("TeleportInstance") retrofit: Retrofit): TeleportService =
            retrofit.create(TeleportService::class.java)
}