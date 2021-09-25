package one.mann.weatherman.common

/* Created by Psmann. */

/** Preferences */
internal const val SETTINGS_UNITS_KEY = "units"
internal const val SETTINGS_UNITS_DEFAULT = "metric"
internal const val SETTINGS_NOTIFICATIONS_KEY = "notification"
internal const val SETTINGS_NOTIFICATIONS_DEFAULT = true
internal const val SETTINGS_FREQUENCY_KEY = "notification_frequency"
internal const val SETTINGS_FREQUENCY_DEFAULT = "24"
internal const val NAVIGATION_GUIDE_KEY = "navigation_guide"
internal const val NAVIGATION_GUIDE_DEFAULT = false
internal const val LAST_UPDATED_KEY = "last_updated_time"
internal const val LAST_UPDATED_DEFAULT = 0L
internal const val LAST_CHECKED_KEY = "last_checked_time"

/** WorkManager */
internal const val NOTIFICATION_WORKER = "NOTIFICATION_WORKER"
internal const val NOTIFICATION_WORKER_TAG = "WORK_COMPLETED"

/** Notification Channel */
internal const val NOTIFICATION_CHANNEL_NAME = "WeatherMan Notifications"
internal const val NOTIFICATION_CHANNEL_ID = "WEATHERMAN_NOTIFICATION"
internal const val NOTIFICATION_ID = 1

/** Bundle */
internal const val PAGER_POSITION = "pager_position"
internal const val PAGER_COUNT = "pager_count"
internal const val ACTIVITY_BACKGROUND = "activity_background"
internal const val DETAIL_BUTTON_CLICKED = "detail_button_clicked"