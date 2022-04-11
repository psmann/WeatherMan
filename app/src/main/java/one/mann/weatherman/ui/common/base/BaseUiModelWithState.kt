package one.mann.weatherman.ui.common.base


/* Created by Psmann. */

interface BaseUiModelWithState<out T> where T : BaseUiModelWithState<T> {

    fun resetState(): T
}