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
                    newBuilder().addQueryParameter(key, value) // Add new query using key-val pair
                            .build()
                }
        val newRequest = oldRequest.newBuilder() // Rebuild the request with query
                .url(queryUrl)
                .build()
        return chain.proceed(newRequest)
    }
}