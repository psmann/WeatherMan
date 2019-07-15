package one.mann.interactors.data.source

interface PreferencesDataSource {

    suspend fun getUnits(): String
}