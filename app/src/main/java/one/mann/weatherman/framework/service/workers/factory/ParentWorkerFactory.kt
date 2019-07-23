package one.mann.weatherman.framework.service.workers.factory

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

internal class ParentWorkerFactory @Inject constructor(
        private val workerFactory: MutableMap<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<CoroutineWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
    ): ListenableWorker? {
        val factoryEntry = workerFactory.entries
                .find { Class.forName(workerClassName).isAssignableFrom(it.key) }

        // Use custom factory for worker if available else use default implementation
        return if (factoryEntry != null) {
            val factoryProvider = factoryEntry.value
            factoryProvider.get().create(appContext, workerParameters)
        } else {
            val workerClass = Class.forName(workerClassName).asSubclass(CoroutineWorker::class.java)
            val constructor =
                    workerClass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
            constructor.newInstance(appContext, workerParameters)
        }
    }
}