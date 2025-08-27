package one.mann.weatherman.di.modules.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import one.mann.weatherman.api.common.Keys
import one.mann.weatherman.api.common.QueryInterceptor
import one.mann.weatherman.api.openweathermap.OwmWeatherService
import one.mann.weatherman.api.timezonedb.TimezoneDbService
import one.mann.weatherman.api.tomtom.TomTomSearchService
import one.mann.weatherman.di.annotations.qualifiers.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/* Created by Psmann. */

@Module
internal class ApiServiceModule {

    companion object {
        private const val OWM_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val TOMTOM_BASE_URL = "https://api.tomtom.com/search/2/search/"
        private const val TIMEZONEDB_BASE_URL = "https://api.timezonedb.com/v2.1/"
        private const val OWM_QUERY_APPID = "appid"
        private const val OWM_QUERY_UNITS = "units"
        private const val OWM_DEFAULT_UNITS = "metric"
        private const val TOMTOM_QUERY_KEY = "key"
        private const val TIMEZONEDB_QUERY_KEY = "key"
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @OpenWeatherMapApi
    fun provideOwmAppIdQueryInterceptor(): QueryInterceptor = QueryInterceptor(OWM_QUERY_APPID, Keys.OWM_APPID)

    @Provides
    @Singleton
    @TomTomApi
    fun provideTomTomKeyQueryInterceptor(): QueryInterceptor = QueryInterceptor(TOMTOM_QUERY_KEY, Keys.TOMTOM_KEY)

    @Provides
    @Singleton
    @TimezoneDbApi
    fun provideTimezoneDbKeyQueryInterceptor(): QueryInterceptor {
        return QueryInterceptor(TIMEZONEDB_QUERY_KEY, Keys.TIMEZONEDB_KEY)
    }

    @Provides
    @Singleton
    @OwmUnits
    fun provideOwmUnitsQueryInterceptor(): QueryInterceptor = QueryInterceptor(OWM_QUERY_UNITS, OWM_DEFAULT_UNITS)

    @Provides
    @Singleton
    @OpenWeatherMapApi
    fun provideOwmOkHttpClient(
        @OpenWeatherMapApi appIdQueryInterceptor: QueryInterceptor,
        @OwmUnits unitsQueryInterceptor: QueryInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(appIdQueryInterceptor)
            .addInterceptor(unitsQueryInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @TomTomApi
    fun provideTomTomOkHttpClient(@TomTomApi keyQueryInterceptor: QueryInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(keyQueryInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @TimezoneDbApi
    fun provideTimezoneDbOkHttpClient(@TimezoneDbApi keyQueryInterceptor: QueryInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(keyQueryInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @OpenWeatherMapApi
    fun provideOwmRestAdapter(
        @OpenWeatherMapApi client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OWM_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @TomTomApi
    fun provideTomTomRestAdapter(
        @TomTomApi client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TOMTOM_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @TimezoneDbApi
    fun provideTimezoneDbRestAdapter(
        @TimezoneDbApi client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TIMEZONEDB_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideOwmService(@OpenWeatherMapApi retrofit: Retrofit): OwmWeatherService {
        return retrofit.create(OwmWeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideTomTomService(@TomTomApi retrofit: Retrofit): TomTomSearchService {
        return retrofit.create(TomTomSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideTimezoneDbService(@TimezoneDbApi retrofit: Retrofit): TimezoneDbService {
        return retrofit.create(TimezoneDbService::class.java)
    }
}
