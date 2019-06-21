package one.mann.weatherman.ui.common.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

internal abstract class BaseViewModel() : ViewModel(), CoroutineScope {

    private var job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @CallSuper // Make children call this implementation if overridden
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}