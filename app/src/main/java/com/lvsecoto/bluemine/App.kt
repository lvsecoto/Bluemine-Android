package com.lvsecoto.bluemine

import android.app.Application
import com.lvsecoto.bluemine.data.network.networkModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(
            networkModule, appModule
        ))
    }
}