package one.mann.weatherman.application.di.module.api

import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [OwmServiceModule::class, TeleportServiceModule::class])
internal class ApiServiceModule {

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
}