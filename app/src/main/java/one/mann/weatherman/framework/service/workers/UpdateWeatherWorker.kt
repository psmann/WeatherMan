package one.mann.weatherman.framework.service.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import one.mann.domain.model.LocationType
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.framework.service.workers.factory.ChildWorkerFactory
import javax.inject.Inject

internal class UpdateWeatherWorker(
        context: Context,
        params: WorkerParameters,
        private val updateWeather: UpdateWeather
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        updateWeather.invoke(LocationType.DB)
        Log.d("FUCK", "success")
        Result.success()
    } catch (e: Exception) {
        Log.d("FUCK", "failure")
        Result.failure()
    }

    class Factory @Inject constructor(private val updateWeather: UpdateWeather) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker =
                UpdateWeatherWorker(appContext, params, updateWeather)
    }
}