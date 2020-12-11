package one.mann.weatherman.framework.data.database

import androidx.room.*
import one.mann.weatherman.framework.data.database.entities.LocationTuple
import one.mann.weatherman.framework.data.database.entities.NotificationTuple
import one.mann.weatherman.framework.data.database.entities.CurrentWeather

/* Created by Psmann. */

// TODO:
//  1. Replace 'cityName' with 'uuid' in delete()
//  2. Replace LocationTuple with LocationCoordinates

@Dao
internal interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeather)

    @Query("SELECT * FROM CurrentWeather ORDER BY position ASC")
    suspend fun fetchAll(): List<CurrentWeather>

    @Query("SELECT cityName, description, currentTemp, day1MinTemp, day1MaxTemp, iconId, sunPosition, humidity, " +
            "hour03Time, hour03IconId, hour03SunPosition, hour06Time, hour06IconId, hour06SunPosition, " +
            "hour09Time, hour09IconId, hour09SunPosition, hour12Time, hour12IconId, hour12SunPosition, " +
            "hour15Time, hour15IconId, hour15SunPosition FROM CurrentWeather WHERE position = 1")
    suspend fun fetchNotificationData(): NotificationTuple

    @Query("SELECT coordinatesLat, coordinatesLong, position FROM CurrentWeather ORDER BY position ASC")
    suspend fun fetchLocations(): List<LocationTuple>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(currentWeathers: List<CurrentWeather>)

    @Query("DELETE FROM CurrentWeather WHERE uuid = :cityId")
    suspend fun delete(cityId: String)
}