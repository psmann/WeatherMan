package one.mann.weatherman.api.openweathermap

import okhttp3.Interceptor
import okhttp3.Response

internal class QueryInterceptor(
        private val key: String,
        private val value: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request() // Intercept original request
        val url = oldRequest.url
                .run {
                    newBuilder().addQueryParameter(key, value) // Add new query with key-val pair
                            .build()
                }
        val newRequest = oldRequest.newBuilder() // Rebuild the request with query
                .url(url)
                .build()
        return chain.proceed(newRequest)
    }
}