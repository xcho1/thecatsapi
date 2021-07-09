package com.thecatapi.cats.network

import com.thecatapi.cats.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderIntercepter : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("x-api-key", BuildConfig.CATS_API_KEY)
            .build()
        return chain.proceed(request)
    }
}