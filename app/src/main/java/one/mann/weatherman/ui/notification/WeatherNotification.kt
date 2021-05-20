package one.mann.weatherman.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import one.mann.domain.logic.DEGREES
import one.mann.domain.logic.removeUnits
import one.mann.domain.logic.roundOff
import one.mann.interactors.usecases.GetNotificationData
import one.mann.weatherman.R
import one.mann.weatherman.common.NOTIFICATION_CHANNEL_ID
import one.mann.weatherman.common.NOTIFICATION_CHANNEL_NAME
import one.mann.weatherman.common.NOTIFICATION_ID
import one.mann.weatherman.ui.common.util.*
import one.mann.weatherman.ui.detail.DetailActivity
import javax.inject.Inject

/* Created by Psmann. */

internal class WeatherNotification @Inject constructor(
    private val context: Context,
    private val getNotificationData: GetNotificationData
) {
    // Pending Intent to open Detail Activity for current location from the notification
    private val detailActivityPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(Intent(context, DetailActivity::class.java))
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /** Get weather data, set up custom layouts, create channel, build notification and display */
    suspend fun show() {
        val data = getNotificationData.invoke().mapToUiNotificationData()
        val currentTemp = data.currentTemp.removeUnits(DEGREES).toFloat().roundOff() + DEGREES
        // Notification layouts
        val notificationCollapsed = RemoteViews(context.packageName, R.layout.notification_collapsed).apply {
            setTextViewText(R.id.notification_current_temp_text_view, currentTemp)
            setTextViewText(R.id.notification_max_temp_text_view, data.day1MaxTemp)
            setTextViewText(R.id.notification_min_temp_text_view, data.day1MinTemp)
            setTextViewText(R.id.notification_city_name_text_view, data.cityName)
            setTextViewText(R.id.notification_description_text_view, data.description)
            setTextViewText(R.id.notification_humidity_text_view, data.humidity)
            setImageViewResource(
                R.id.notification_icon_image_view, context.resources.getIdentifier(
                    getUri(data.iconId, data.sunPosition), "drawable", context.packageName
                )
            )
        }
        val notificationExpanded = RemoteViews(context.packageName, R.layout.notification_expanded).apply {
            setTextViewText(R.id.notification_current_temp_text_view, currentTemp)
            setTextViewText(R.id.notification_max_temp_text_view, data.day1MaxTemp)
            setTextViewText(R.id.notification_min_temp_text_view, data.day1MinTemp)
            setTextViewText(R.id.notification_city_name_text_view, data.cityName)
            setTextViewText(R.id.notification_description_text_view, data.description)
            setTextViewText(R.id.notification_humidity_text_view, data.humidity)
            setImageViewResource(
                R.id.notification_icon_image_view, context.resources.getIdentifier(
                    getUri(data.iconId, data.sunPosition), "drawable", context.packageName
                )
            )
            setTextViewText(R.id.notification_temp_forecast1_text_view, data.hour03Time)
            setTextViewText(R.id.notification_temp_forecast2_text_view, data.hour06Time)
            setTextViewText(R.id.notification_temp_forecast3_text_view, data.hour09Time)
            setTextViewText(R.id.notification_temp_forecast4_text_view, data.hour12Time)
            setTextViewText(R.id.notification_temp_forecast5_text_view, data.hour15Time)
            setImageViewResource(
                R.id.notification_icon_forecast1_image_view, context.resources.getIdentifier(
                    getUri(data.hour03IconId, data.hour03SunPosition), "drawable", context.packageName
                )
            )
            setImageViewResource(
                R.id.notification_icon_forecast2_image_view, context.resources.getIdentifier(
                    getUri(data.hour06IconId, data.hour06SunPosition), "drawable", context.packageName
                )
            )
            setImageViewResource(
                R.id.notification_icon_forecast3_image_view, context.resources.getIdentifier(
                    getUri(data.hour09IconId, data.hour09SunPosition), "drawable", context.packageName
                )
            )
            setImageViewResource(
                R.id.notification_icon_forecast4_image_view, context.resources.getIdentifier(
                    getUri(data.hour12IconId, data.hour12SunPosition), "drawable", context.packageName
                )
            )
            setImageViewResource(
                R.id.notification_icon_forecast5_image_view, context.resources.getIdentifier(
                    getUri(data.hour15IconId, data.hour15SunPosition), "drawable", context.packageName
                )
            )
        }
        // Create notification channel if necessary
        makeNotificationChannel()
        // Build notification and show
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setCustomContentView(notificationCollapsed)
            .setCustomBigContentView(notificationExpanded)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(detailActivityPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVibrate(LongArray(0))
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .run { NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, this.build()) }
    }

    /** Create notification channel, re-creating an existing channel performs no operation */
    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?).run {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_DEFAULT)
                        .apply {
                            this.description = context.getString(R.string.notification_channel_description)
                            this.lockscreenVisibility = Notification.VISIBILITY_SECRET
                        }
                this?.createNotificationChannel(channel)
            }
        }
    }
}