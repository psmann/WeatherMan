package one.mann.weatherman.ui.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import one.mann.interactors.usecases.GetNotificationData
import one.mann.weatherman.R
import one.mann.weatherman.ui.common.util.NOTIFICATION_CHANNEL_DESCRIPTION
import one.mann.weatherman.ui.common.util.NOTIFICATION_CHANNEL_ID
import one.mann.weatherman.ui.common.util.NOTIFICATION_CHANNEL_NAME
import one.mann.weatherman.ui.common.util.NOTIFICATION_ID
import one.mann.weatherman.ui.main.MainActivity
import javax.inject.Inject

internal class WeatherNotification @Inject constructor(
        private val context: Context,
        private val getNotificationData: GetNotificationData
) {

    suspend fun showNotification() {

        val notificationCollapsed = RemoteViews(context.packageName, R.layout.notification_collapsed)
        val data = getNotificationData.invoke()

        notificationCollapsed.setTextViewText(R.id.notification_temperature, data.currentTemp)
        notificationCollapsed.setTextViewText(R.id.notification_city_name, data.cityName)
        notificationCollapsed.setTextViewText(R.id.notification_humidity, "50%")
        notificationCollapsed.setTextViewText(R.id.notification_wind_speed, "10 Km/Hr")
        notificationCollapsed.setImageViewResource(R.id.notification_icon, R.drawable.ic_launcher_foreground)

        makeNotificationChannel()
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(notificationCollapsed)
                .setContentTitle(data.description)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        Intent(context, MainActivity::class.java), 0))
                .setContentText("Temperature in ${data.cityName} is ${data.currentTemp}")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVibrate(LongArray(0))
                .run { NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, this.build()) }
    }

    /** Create a notification channel if necessary, creating an existing notification channel performs no operation */
    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?).run {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT)
                        .apply { this.description = NOTIFICATION_CHANNEL_DESCRIPTION }
                this?.createNotificationChannel(channel)
            }
    }
}