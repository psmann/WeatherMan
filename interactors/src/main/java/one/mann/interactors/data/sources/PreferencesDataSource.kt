package one.mann.interactors.data.sources

/* Created by Psmann. */

interface PreferencesDataSource {

    suspend fun getUnits(): String

    suspend fun getLastChecked(): Long
}