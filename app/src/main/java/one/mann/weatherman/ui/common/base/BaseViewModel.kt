package one.mann.weatherman.ui.common.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/* Created by Psmann. */

internal abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob() // Doesn't get cancelled when a child coroutine crashes
    var errorMessage: (String) -> Unit = {} // Pass error message to coroutine by implementing this function
    private val exceptionHandler = CoroutineExceptionHandler { _, e -> errorMessage(e.message.toString()) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + exceptionHandler

    /** Make children call this implementation if overridden */
    @CallSuper
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}