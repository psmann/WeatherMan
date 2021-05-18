package one.mann.weatherman.api.common

import okhttp3.Interceptor
import okhttp3.Response

/* Created by Psmann. */

internal class QueryInterceptor(
    private var key: String,
    private var value: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request() // Original request
        val queryUrl = oldRequest.url
            .run {
                // Add new query using key-val pair
                newBuilder().addQueryParameter(key, value)
                    .build()
            }
        // Rebuild the request with query
        val newRequest = oldRequest.newBuilder()
            .url(queryUrl)
            .build()

        return chain.proceed(newRequest)
    }
}