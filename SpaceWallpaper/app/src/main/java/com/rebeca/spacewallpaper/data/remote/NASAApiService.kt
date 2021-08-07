package com.rebeca.spacewallpaper.data.remote

import com.rebeca.spacewallpaper.BuildConfig
import com.rebeca.spacewallpaper.SpaceImage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// todo get api key specific to user, could I use demo key? learn to use proxy to api keys
const val BASE_URL = "https://api.nasa.gov/"

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { apiKeyInterceptor(it) }
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi)) //to format getImageOfDay
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface NASAApiService {
    @GET("planetary/apod")
    suspend fun getImageOfDay(): // suspend will let function to run in a coroutine scope
            SpaceImage
}

object NASAApi {
    val retrofitService : NASAApiService by lazy {
        retrofit.create(NASAApiService::class.java)
    }
}

private fun apiKeyInterceptor(it: Interceptor.Chain): Response {
    val originalRequest = it.request()
    val originalHttpUrl = originalRequest.url()
    val newHttpUrl = originalHttpUrl.newBuilder()
        .addQueryParameter("api_key", BuildConfig.API_KEY)
        .build()
    val newRequest = originalRequest.newBuilder()
        .url(newHttpUrl)
        .build()
    return it.proceed(newRequest)
}