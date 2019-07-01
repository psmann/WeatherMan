package one.mann.weatherman.application.di.module.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import one.mann.weatherman.application.di.annotation.ViewModelKey
import one.mann.weatherman.ui.common.base.ViewModelFactory
import one.mann.weatherman.ui.main.MainViewModel

@Module
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}