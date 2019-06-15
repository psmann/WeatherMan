package one.mann.weatherman.framework.data.database

import androidx.room.*

@Dao
interface WeatherDao {

    @Query("SELECT COUNT(id) FROM Weather")
    fun tableSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: Weather)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(weathers: List<Weather>)

    @Query("SELECT * FROM Weather ORDER BY id ASC")
    fun getAll(): List<Weather>

    @Query("SELECT coordinatesLat, coordinatesLong FROM Weather ORDER BY id ASC")
    fun getAllLocations(): List<LocationTuple>

    @Query("DELETE FROM Weather WHERE id = :id")
    fun delete(id: Int)
}