package com.lvsecoto.bluemine.data.network

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lvsecoto.bluemine.BuildConfig
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.LiveDataCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HOST_NAME = BuildConfig.TEST_REDMINE_HOST_NAME
const val USER_NAME = BuildConfig.TEST_REDMINE_USER_NAME
const val USER_PASSWORD = BuildConfig.TEST_REDMINE_PASSWORD

val networkModule = module {
    single { createGson() }
    single { createClient() }
    single { createRetrofit(get()) }
    single { createRedMineService(get()) }
}

fun createGson(): Gson =
    GsonBuilder()
        .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setPrettyPrinting()
        .create()

fun createRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl("http://hostPlaceholder")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

fun createClient(): OkHttpClient =
    OkHttpClient()
        .newBuilder()
        .addInterceptor { chain: Interceptor.Chain? ->
            chain!!.request().let {

                val urlWithRealHostNameAndAuthor = it.url()
                    .newBuilder()
                    .host(HOST_NAME)
                    .username(USER_NAME)
                    .password(USER_PASSWORD)
                    .build()

                it.newBuilder()
                    .url(urlWithRealHostNameAndAuthor)
                    .build()

            }.let { chain.proceed(it) }
        }
        .addNetworkInterceptor { chain ->
            val requestUrl = chain.request().url().toString()
            val response = chain.proceed(chain.request())
            val responseBody = response.peekBody(1000 * 1000).string()
            val responseJson = JSONObject(responseBody).toString(2)
            Log.e(
                "NetworkLog", """======
url: $requestUrl
body: $responseJson
"""
            )

            response
        }
        .build()

fun createRedMineService(retrofit: Retrofit): RedMineService =
    retrofit.create(RedMineService::class.java)

