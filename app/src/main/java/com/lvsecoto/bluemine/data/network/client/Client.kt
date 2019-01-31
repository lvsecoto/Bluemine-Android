package com.lvsecoto.bluemine.data.network.client

import android.content.Context
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.lvsecoto.bluemine.BuildConfig
import com.lvsecoto.bluemine.data.repo.SettingsRepository
import okhttp3.OkHttpClient

const val HOST_NAME = BuildConfig.TEST_REDMINE_HOST_NAME
const val USER_NAME = BuildConfig.TEST_REDMINE_USER_NAME
const val USER_PASSWORD = BuildConfig.TEST_REDMINE_PASSWORD

fun createClient(context: Context, repo: SettingsRepository): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(getHostNameInterceptor(repo))
        .addInterceptor(getAuthorInterceptor(repo))
        .addNetworkInterceptor(getGetLoggerInterceptor())
        .cookieJar(getCookieJar(context))
        .build()
}

private fun getCookieJar(context: Context): ClearableCookieJar = PersistentCookieJar(
    SetCookieCache(),
    SharedPrefsCookiePersistor(context)
)

