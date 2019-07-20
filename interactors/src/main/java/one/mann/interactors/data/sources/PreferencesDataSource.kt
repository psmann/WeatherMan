package one.mann.interactors.data.sources

interface PreferencesDataSource {

    suspend fun getUnits(): String
}