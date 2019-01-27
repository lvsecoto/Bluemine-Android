package com.lvsecoto.bluemine.data.cache.db

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val dbModule = module {
    single { createAppDatabase(androidContext(), get()) }
    single { createAppDao(get()) }
}
