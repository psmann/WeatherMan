package one.mann.weatherman.ui.main.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import one.mann.weatherman.ui.common.di.ViewModelKey
import one.mann.weatherman.ui.main.MainViewModel

@Module
internal abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}