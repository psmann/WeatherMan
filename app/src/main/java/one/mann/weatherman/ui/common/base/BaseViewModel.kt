package one.mann.weatherman.ui.common.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/* Created by Psmann. */

internal abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob() // SupervisorJob doesn't get cancelled when a child coroutine crashes
    protected open val exceptionResponse: (String) -> Unit = {} // Handle response to coroutine exception
    private val exceptionHandler = CoroutineExceptionHandler { _, e -> exceptionResponse(e.message.toString()) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + exceptionHandler

    /** Super must be called if overridden */
    @CallSuper
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}