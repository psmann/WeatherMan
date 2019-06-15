package one.mann.interactors.data.source

interface IApiTimezoneSource {

    suspend fun getTimezone(location: Array<Float>): String
}