package one.mann.weatherman.framework.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import one.mann.domain.model.LocationType
import one.mann.interactors.usecases.UpdateWeather
import one.mann.weatherman.application.WeatherManApp
import javax.inject.Inject

class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    @Inject
    lateinit var updateWeather: UpdateWeather

    init {
        WeatherManApp.appComponent.getMainComponent().injectWorker(this)
    }

    override suspend fun doWork(): Result = try {
        updateWeather.invoke(LocationType.DB)
        Log.d("FUCK", "success")
        Result.success()
    } catch (e: Exception) {
        Log.d("FUCK", "failure")
        Result.failure()
    }
}