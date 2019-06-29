package one.mann.weatherman.api.common.di

import dagger.Module
import dagger.Provides
import one.mann.weatherman.api.openweathermap.di.OwmServiceModule
import one.mann.weatherman.api.teleport.di.TeleportServiceModule
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [OwmServiceModule::class, TeleportServiceModule::class])
internal class ApiServiceModule {

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
}