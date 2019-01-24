package com.lvsecoto.bluemine.data.repo

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val repoModule = module {
    single { Repository(get(), get(), androidContext().getSharedPreferences("repo", Context.MODE_PRIVATE)) }
}