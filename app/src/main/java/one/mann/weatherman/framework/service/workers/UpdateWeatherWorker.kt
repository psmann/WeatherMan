package one.mann.weatherman.framework.service.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import one.mann.domain.model.LocationType
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.framework.service.workers.factory.ChildWorkerFactory
import javax.inject.Inject

internal class UpdateWeatherWorker(
        private val updateWeather: UpdateWeather,
        context: Context,
        params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        updateWeather.invoke(LocationType.DB)
        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }

    class Factory @Inject constructor(private val updateWeather: UpdateWeather) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker =
                UpdateWeatherWorker(updateWeather, appContext, params)
    }
}