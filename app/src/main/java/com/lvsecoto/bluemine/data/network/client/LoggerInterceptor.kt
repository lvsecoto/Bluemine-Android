package com.lvsecoto.bluemine.data.network.client

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject

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
