package com.lvsecoto.bluemine.data.network

import android.content.Context
import android.util.Log
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lvsecoto.bluemine.BuildConfig
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.LiveDataCallAdapterFactory
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okio.Buffer
import org.json.JSONObject
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val HOST_NAME = BuildConfig.TEST_REDMINE_HOST_NAME
const val USER_NAME = BuildConfig.TEST_REDMINE_USER_NAME
const val USER_PASSWORD = BuildConfig.TEST_REDMINE_PASSWORD

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
        .baseUrl("http://hostPlaceholder")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()


fun createClient(context: Context): OkHttpClient {
    val cookieJar: ClearableCookieJar = PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(context)
    )

    return OkHttpClient()
        .newBuilder()
        .addInterceptor { chain: Interceptor.Chain? ->
            chain!!.request().let {

                val urlWithRealHostNameAndAuthor = it.url()
                    .newBuilder()
                    .host(HOST_NAME)
                    .build()

                it.newBuilder()
                    .url(urlWithRealHostNameAndAuthor)
                    .build()

            }.let { chain.proceed(it) }
        }
        .addInterceptor { chain ->
            val credential = Credentials.basic(USER_NAME, USER_PASSWORD)
            val request = chain.request()
                .newBuilder()
                .header("Authorization", credential)
                .build()

            chain.proceed(request)
        }
        .cookieJar(cookieJar)
        .addNetworkInterceptor { chain ->

            val requestUrl = chain.request().url().toString()
            val body = try {
                val buffer =  Buffer()
                chain.request().body()?.writeTo(buffer).run {
                    buffer.readUtf8()
                }
            } catch (e: Exception) {
                null
            }

            val response = chain.proceed(chain.request())
            val responseBody = response.peekBody(1000 * 1000).string()
            val responseJson = try {
                JSONObject(responseBody).toString(2) ?: responseBody
            } catch (e: Exception) {
                responseBody
            }
            val responseCode = response.code()
            Log.e(
                "NetworkLog", """======
url: $requestUrl
request body:
${(body?:"").prependIndent("  ")}
body:
${responseJson.prependIndent("  ")}
code: $responseCode
"""
            )

            response
        }
        .build()
}

fun createRedMineService(retrofit: Retrofit): RedMineService =
    retrofit.create(RedMineService::class.java)

