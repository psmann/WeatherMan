package one.mann.weatherman.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import one.mann.domain.logic.roundOff
import one.mann.interactors.usecases.GetNotificationData
import one.mann.weatherman.R
import one.mann.weatherman.ui.common.util.*
import one.mann.weatherman.ui.main.MainActivity
import javax.inject.Inject

internal class WeatherNotification @Inject constructor(
        private val context: Context,
        private val getNotificationData: GetNotificationData
) {
    /** Get updated data, set up custom layouts, create channel, build notification and display */
    suspend fun show() {
        val data = getNotificationData.invoke()
        val notificationCollapsed = RemoteViews(context.packageName, R.layout.notification_collapsed)
        val notificationExpanded = RemoteViews(context.packageName, R.layout.notification_expanded)
        val curTemp = data.currentTemp.replace("C", "").toFloat().roundOff()
        val maxTemp = data.maxTemp.replace("C", "").toFloat().roundOff()
        val minTemp = data.minTemp.replace("C", "").toFloat().roundOff()
        // Set up layout for collapsed notification
        notificationCollapsed.setTextViewText(R.id.notification_temp, "$curTemp°")
        notificationCollapsed.setTextViewText(R.id.notification_max_temp, "$maxTemp°")
        notificationCollapsed.setTextViewText(R.id.notification_min_temp, "$minTemp°")
        notificationCollapsed.setTextViewText(R.id.notification_city_name, data.cityName)
        notificationCollapsed.setTextViewText(R.id.notification_description, data.description)
        notificationCollapsed.setTextViewText(R.id.notification_humidity, data.humidity)
        notificationCollapsed.setImageViewResource(R.id.notification_icon, context.resources
                .getIdentifier(getUri(data.iconId, data.sunPosition), "drawable", context.packageName))
        // Set up layout for expanded notification
        notificationExpanded.setTextViewText(R.id.notification_temp, "$curTemp°")
        notificationExpanded.setTextViewText(R.id.notification_max_temp, "$maxTemp°")
        notificationExpanded.setTextViewText(R.id.notification_min_temp, "$minTemp°")
        notificationExpanded.setTextViewText(R.id.notification_city_name, data.cityName)
        notificationExpanded.setTextViewText(R.id.notification_description, data.description)
        notificationExpanded.setTextViewText(R.id.notification_humidity, data.humidity)
        notificationExpanded.setImageViewResource(R.id.notification_icon, context.resources
                .getIdentifier(getUri(data.iconId, data.sunPosition), "drawable", context.packageName))
        notificationExpanded.setTextViewText(R.id.notification_temp_forecast1, data.hour03Time)
        notificationExpanded.setTextViewText(R.id.notification_temp_forecast2, data.hour06Time)
        notificationExpanded.setTextViewText(R.id.notification_temp_forecast3, data.hour09Time)
        notificationExpanded.setTextViewText(R.id.notification_temp_forecast4, data.hour12Time)
        notificationExpanded.setTextViewText(R.id.notification_temp_forecast5, data.hour15Time)
        notificationExpanded.setImageViewResource(R.id.notification_icon_forecast1, context.resources
                .getIdentifier(getUri(data.hour03IconId, data.hour03SunPosition), "drawable", context.packageName))
        notificationExpanded.setImageViewResource(R.id.notification_icon_forecast2, context.resources
                .getIdentifier(getUri(data.hour06IconId, data.hour06SunPosition), "drawable", context.packageName))
        notificationExpanded.setImageViewResource(R.id.notification_icon_forecast3, context.resources
                .getIdentifier(getUri(data.hour09IconId, data.hour09SunPosition), "drawable", context.packageName))
        notificationExpanded.setImageViewResource(R.id.notification_icon_forecast4, context.resources
                .getIdentifier(getUri(data.hour12IconId, data.hour12SunPosition), "drawable", context.packageName))
        notificationExpanded.setImageViewResource(R.id.notification_icon_forecast5, context.resources
                .getIdentifier(getUri(data.hour15IconId, data.hour15SunPosition), "drawable", context.packageName))
        // Create notification channel if necessary
        makeNotificationChannel()
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setCustomContentView(notificationCollapsed)
                .setCustomBigContentView(notificationExpanded)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        Intent(context, MainActivity::class.java), 0))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVibrate(LongArray(0))
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .run { NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, this.build()) }
    }

    /** Create notification channel, re-creating an existing channel performs no operation */
    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?).run {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT)
                        .apply {
                            this.description = NOTIFICATION_CHANNEL_DESCRIPTION
                            this.lockscreenVisibility = Notification.VISIBILITY_SECRET
                        }
                this?.createNotificationChannel(channel)
            }
    }
}