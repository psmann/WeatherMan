package one.mann.weatherman.framework.data.database

import androidx.room.*
import one.mann.weatherman.framework.data.database.model.LocationTuple
import one.mann.weatherman.framework.data.database.model.NotificationTuple
import one.mann.weatherman.framework.data.database.model.Weather

@Dao
internal interface WeatherDao {

    @Query("SELECT COUNT(id) FROM Weather")
    suspend fun tableSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: Weather)

    @Query("SELECT * FROM Weather ORDER BY id ASC")
    suspend fun fetchAll(): List<Weather>

    @Query("SELECT cityName, currentTemp, description FROM Weather WHERE id = 1")
    suspend fun fetchNotificationData(): NotificationTuple

    @Query("SELECT coordinatesLat, coordinatesLong, id FROM Weather ORDER BY id ASC")
    suspend fun fetchLocations(): List<LocationTuple>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(weathers: List<Weather>)

    @Query("DELETE FROM Weather WHERE cityName = :name")
    suspend fun delete(name: String)
}