package one.mann.weatherman.framework.service.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import one.mann.domain.models.location.LocationType
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.framework.service.workers.factory.ChildWorkerFactory
import one.mann.weatherman.ui.common.util.isLocationEnabled
import one.mann.weatherman.ui.notification.WeatherNotification
import javax.inject.Inject

/* Created by Psmann. */

internal class NotificationWorker(
        private val updateWeather: UpdateWeather,
        private val weatherNotification: WeatherNotification,
        private val context: Context,
        params: WorkerParameters
) : CoroutineWorker(context, params) {

    /** Tasks are enqueued inside a single Worker because PeriodicWork doesn't allow chaining of Workers */
    override suspend fun doWork(): Result = try {
        // Use GPS if location services are enabled otherwise use previously saved location
        updateWeather.invoke(if (context.isLocationEnabled()) LocationType.DEVICE else LocationType.DB)
        // Show notification
        weatherNotification.show()
        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }

    class Factory @Inject constructor(
            private val updateWeather: UpdateWeather,
            private val weatherNotification: WeatherNotification
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return NotificationWorker(updateWeather, weatherNotification, appContext, params)
        }
    }
}