package one.mann.weatherman.framework.service.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import one.mann.domain.model.LocationType
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.framework.service.workers.factory.ChildWorkerFactory
import one.mann.weatherman.ui.notification.WeatherNotification
import javax.inject.Inject

internal class NotificationWorker(
        private val updateWeather: UpdateWeather,
        private val weatherNotification: WeatherNotification,
        context: Context,
        params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        // Multiple tasks enqueued inside a single Worker class as a workaround
        // since PeriodicWork doesn't allow chaining of multiple workers
        updateWeather.invoke(LocationType.DB)
        weatherNotification.showNotification()
        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }

    class Factory @Inject constructor(
            private val updateWeather: UpdateWeather,
            private val weatherNotification: WeatherNotification
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker =
                NotificationWorker(updateWeather, weatherNotification, appContext, params)
    }
}