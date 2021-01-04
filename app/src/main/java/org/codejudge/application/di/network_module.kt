package org.codejudge.application.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.codejudge.application.BuildConfig
import org.codejudge.application.domain.network.HomeApiService
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created Koin module for Network classes
 */
val networkModule = module {

    single { createOkHttpClient() }

    single { createWebService<HomeApiService>(get(), BuildConfig.SERVER_URL_NEW) }

}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    val client = OkHttpClient.Builder()
        .connectTimeout(120L, TimeUnit.SECONDS)
        .readTimeout(120L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()
    return client
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}