package com.lvsecoto.bluemine.data.network.client

import android.content.Context

import android.util.Log
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.lvsecoto.bluemine.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject

const val HOST_NAME = BuildConfig.TEST_REDMINE_HOST_NAME
const val USER_NAME = BuildConfig.TEST_REDMINE_USER_NAME
const val USER_PASSWORD = BuildConfig.TEST_REDMINE_PASSWORD

fun createClient(context: Context): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(getHostNameInterceptor())
        .addInterceptor(getGetAuthorInterceptor())
        .cookieJar(getCookieJar(context))
        .addNetworkInterceptor(getGetLoggerInterceptor())
        .build()
}

private fun getCookieJar(context: Context): ClearableCookieJar = PersistentCookieJar(
    SetCookieCache(),
    SharedPrefsCookiePersistor(context)
)


private fun getHostNameInterceptor() = { chain: Interceptor.Chain? ->
    val localHosts = arrayOf(
        "127.0.0.1(:\\d+)?",
        "localhost"
    )

    chain!!.request().let {

        val urlWithRealHostNameAndAuthor = it.url()
            .newBuilder()
            .apply {
                if(localHosts.any { hostName -> it.url().host().matches(hostName.toRegex()) }
                ) {
                    host(HOST_NAME)
                    port(80)
                }
            }
            .build()

        it.newBuilder()
            .url(urlWithRealHostNameAndAuthor)
            .build()

    }.let { chain.proceed(it) }
}

fun getGetAuthorInterceptor(): (Interceptor.Chain) -> Response = { chain ->
    val credential = Credentials.basic(
        USER_NAME,
        USER_PASSWORD
    )
    val request = chain.request()
        .newBuilder()
        .header("Authorization", credential)
        .build()
    chain.proceed(request)
}

fun getGetLoggerInterceptor(): (Interceptor.Chain) -> Response = { chain ->

    val requestUrl = chain.request().url().toString()
    val body = try {
        val buffer = Buffer()
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
${(body ?: "").prependIndent("  ")}
body:
${responseJson.prependIndent("  ")}
code: $responseCode
"""
    )

    response
}
