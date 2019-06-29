package one.mann.weatherman.api.common.di

import dagger.Module
import dagger.Provides
import one.mann.weatherman.api.openweathermap.di.OwmModule
import one.mann.weatherman.api.teleport.di.TeleportModule
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OwmModule::class, TeleportModule::class])
internal class ApiModule {

    @Provides
//    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
}