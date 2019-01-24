package com.lvsecoto.bluemine.data.network

import com.lvsecoto.bluemine.BuildConfig
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.LiveDataCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HOST_NAME = BuildConfig.TEST_REDMINE_HOST_NAME
const val USER_NAME = BuildConfig.TEST_REDMINE_USER_NAME
const val USER_PASSWORD = BuildConfig.TEST_REDMINE_PASSWORD

val networkModule = module {
    single { createClient() }
    single { createRetrofit(get()) }
    single { createIssuesService(get()) }
}

fun createRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl("http://hostPlaceholder")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

fun createClient(): OkHttpClient = OkHttpClient()
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
    .build()

fun createIssuesService(retrofit: Retrofit): RedMineService =
    retrofit.create(RedMineService::class.java)

