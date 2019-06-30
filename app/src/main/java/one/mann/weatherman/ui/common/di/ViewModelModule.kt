package one.mann.weatherman.ui.common.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import one.mann.weatherman.ui.common.base.ViewModelFactory
import one.mann.weatherman.ui.main.di.MainViewModelModule

@Module(includes = [MainViewModelModule::class])
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}