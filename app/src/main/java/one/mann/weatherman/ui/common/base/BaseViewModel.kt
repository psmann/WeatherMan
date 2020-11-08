package one.mann.weatherman.ui.common.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/* Created by Psmann. */

internal abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob() // Doesn't get cancelled when a child coroutine crashes
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /** Make children call this implementation if overridden */
    @CallSuper
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}