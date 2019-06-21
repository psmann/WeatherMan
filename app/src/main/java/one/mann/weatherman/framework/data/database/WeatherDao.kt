package one.mann.weatherman.framework.data.database

import androidx.room.*

@Dao
internal interface WeatherDao {

    @Query("SELECT COUNT(id) FROM Weather")
    suspend fun tableSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: Weather)

    @Query("SELECT * FROM Weather ORDER BY id ASC")
    suspend fun getAll(): List<Weather>

    @Query("SELECT coordinatesLat, coordinatesLong FROM Weather ORDER BY id ASC")
    suspend fun getAllLocations(): List<LocationTuple>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(weathers: List<Weather>)

    @Query("DELETE FROM Weather WHERE cityName = :name")
    suspend fun delete(name: String)
}