package com.lvsecoto.bluemine.data.network.client

import com.lvsecoto.bluemine.data.repo.SettingsRepository
import okhttp3.Interceptor

fun getHostNameInterceptor(repo: SettingsRepository) = { chain: Interceptor.Chain? ->
    val localHosts = arrayOf(
        "127.0.0.1(:\\d+)?",
        "localhost"
    )

    chain!!.request().let {

        val urlWithHostNameAndPort = it.url()
            .newBuilder()
            .apply {
                if (localHosts.any { hostName -> it.url().host().matches(hostName.toRegex()) }
                ) {
                    host(repo.retrofitSetting?.hostName ?: HOST_NAME)
                    port(repo.retrofitSetting?.port ?: 80)
                }
            }
            .build()

        it.newBuilder()
            .url(urlWithHostNameAndPort)
            .build()

    }.let { chain.proceed(it) }
}

