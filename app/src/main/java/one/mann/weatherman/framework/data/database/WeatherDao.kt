package one.mann.weatherman.framework.data.database

import androidx.room.*
import one.mann.weatherman.framework.data.database.entities.*
import one.mann.weatherman.framework.data.database.entities.relations.*

/* Created by Psmann. */

@Dao
internal interface WeatherDao {

    /** Inserts a City */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    /** Inserts a CurrentWeather */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    /** Inserts a list of CurrentWeathers */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCurrentWeathers(currentWeathers: List<CurrentWeather>)

    /** Inserts a list of HourlyForecast */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecasts(hourlyForecasts: List<HourlyForecast>)

    /** Inserts a list of DailyForecast */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecasts(dailyForecasts: List<DailyForecast>)

    /** Gets user city */
    @Query("SELECT * FROM City ORDER BY timeCreated ASC LIMIT 1")
    suspend fun getUserCity(): City

    /** Gets all cities */
    @Query("SELECT * FROM City ORDER BY timeCreated ASC")
    suspend fun getAllCities(): List<City>

    /** Gets CurrentWeather for a City */
    @Transaction
    @Query("SELECT * FROM City WHERE cityId = :cityId")
    suspend fun getCurrentWeather(cityId: String): CityWithCurrentWeather

    /** Gets all CurrentWeathers */
    @Query("SELECT * FROM CurrentWeather")
    suspend fun getAllCurrentWeather(): List<CurrentWeather>

    /** Gets HourlyForecasts for a City */
    @Transaction
    @Query("SELECT * FROM City WHERE cityId = :cityId")
    suspend fun getHourlyForecasts(cityId: String): CityWithHourlyForecasts

    /** Gets DailyForecasts for a City */
    @Transaction
    @Query("SELECT * FROM City WHERE cityId = :cityId")
    suspend fun getCDailyForecasts(cityId: String): CityWithDailyForecasts

    /** Returns the City for user location */
    @Query("SELECT * FROM City WHERE timeCreated = (SELECT MIN(timeCreated) FROM City)")
    suspend fun getCityNameForUserLocation(): City

    /** Gets today's forecast for user location */
    @Transaction
    @Query("SELECT * FROM DailyForecast WHERE cityId = :cityId")
    suspend fun getTodayForecastForUserLocation(cityId: String): DailyForecast

    /** Gets CurrentWeather and HourlyForecast for user location */
    @Transaction
    @Query("SELECT * FROM CurrentWeather WHERE cityId = :cityId")
    suspend fun getHourlyForecastsForUserLocation(cityId: String): CurrentWeatherWithHourlyForecasts

    /** Updates City entity (returns 1 if successful) */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCity(city: City)

    /** Updates CurrentWeather entity */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentWeathers(currentWeathers: List<CurrentWeather>)

    /** Updates HourlyForecast entity */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHourlyForecasts(hourlyForecasts: List<HourlyForecast>)

    /** Updates DailyForecast entity */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDailyForecasts(dailyForecasts: List<DailyForecast>)

    /** Deletes City with the passed cityId */
    @Query("DELETE FROM City WHERE cityId = :cityId")
    suspend fun deleteCity(cityId: String)

    /** Deletes CurrentWeather with the passed cityId */
    @Query("DELETE FROM CurrentWeather WHERE cityId = :cityId")
    suspend fun deleteCurrentWeather(cityId: String)

    /** Deletes HourlyForecasts with the passed cityId */
    @Query("DELETE FROM HourlyForecast WHERE cityId = :cityId")
    suspend fun deleteHourlyForecasts(cityId: String)

    /** Deletes DailyForecasts with the passed cityId */
    @Query("DELETE FROM DailyForecast WHERE cityId = :cityId")
    suspend fun deleteDailyForecasts(cityId: String)
}