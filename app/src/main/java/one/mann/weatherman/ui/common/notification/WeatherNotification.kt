package one.mann.weatherman.ui.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import one.mann.interactors.usecases.GetNotificationData
import one.mann.weatherman.R
import one.mann.weatherman.ui.common.util.NOTIFICATION_CHANNEL_DESCRIPTION
import one.mann.weatherman.ui.common.util.NOTIFICATION_CHANNEL_ID
import one.mann.weatherman.ui.common.util.NOTIFICATION_CHANNEL_NAME
import one.mann.weatherman.ui.common.util.NOTIFICATION_ID
import javax.inject.Inject

internal class WeatherNotification @Inject constructor(
        private val context: Context,
        private val getNotificationData: GetNotificationData
) {

    suspend fun showNotification() {
        makeNotificationChannel()
        val data = getNotificationData.invoke()
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(data.description)
                .setContentText("Temperature in ${data.cityName} is ${data.currentTemp}")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVibrate(LongArray(0))
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

    /** Create a notification channel if necessary, creating an existing notification channel performs no operation */
    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)
                    .apply { this.description = NOTIFICATION_CHANNEL_DESCRIPTION }
            manager?.createNotificationChannel(channel)
        }
    }
}