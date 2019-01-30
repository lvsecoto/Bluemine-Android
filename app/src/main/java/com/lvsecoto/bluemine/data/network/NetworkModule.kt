package com.lvsecoto.bluemine.data.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lvsecoto.bluemine.data.network.client.createClient
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { createGson() }
    single { createClient(get()) }
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
        .baseUrl("http://localhost")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

fun createRedMineService(retrofit: Retrofit): RedMineService =
    retrofit.create(RedMineService::class.java)

