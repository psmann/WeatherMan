package one.mann.weatherman.application.di.module.ui

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import one.mann.weatherman.ui.common.base.ViewModelFactory

@Module(includes = [MainViewModelModule::class])
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}