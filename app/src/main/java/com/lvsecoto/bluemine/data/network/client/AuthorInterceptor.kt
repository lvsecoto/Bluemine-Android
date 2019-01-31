package com.lvsecoto.bluemine.data.network.client

import com.lvsecoto.bluemine.data.repo.SettingsRepository
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

fun getAuthorInterceptor(repo: SettingsRepository): (Interceptor.Chain) -> Response = { chain ->
    val credential = Credentials.basic(
        repo.retrofitSetting?.userName ?: USER_NAME,
        repo.retrofitSetting?.password ?: USER_PASSWORD
    )
    val request = chain.request()
        .newBuilder()
        .header("Authorization", credential)
        .build()
    chain.proceed(request)
}

