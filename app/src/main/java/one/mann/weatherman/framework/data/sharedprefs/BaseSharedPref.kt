package one.mann.weatherman.framework.data.sharedprefs

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.reflect.KClass

class BaseSharedPref<T : Any>(private val preferences: SharedPreferences,
                              private val entity: KClass<T>,
                              private val key: String) {

    private val gson = Gson()

    fun save(t: T) = preferences.edit()
            .putString(key, gson.toJson(t)) // convert to json to save
            .apply()

    fun open(): T = gson.fromJson(preferences.getString(key, ""), entity.java)

    fun delete() = preferences.edit()
            .remove(key)
            .apply()
}