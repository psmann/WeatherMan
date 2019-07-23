package one.mann.weatherman.framework.service.workers.factory

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

internal interface ChildWorkerFactory {

    fun create(appContext: Context, params: WorkerParameters): CoroutineWorker
}