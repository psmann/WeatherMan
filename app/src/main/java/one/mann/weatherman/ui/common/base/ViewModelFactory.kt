package one.mann.weatherman.ui.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

internal class ViewModelFactory @Inject constructor(
        private val viewModel: MutableMap<Class<out ViewModel>,
                @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel[modelClass]?.get() as T
}