package com.lvsecoto.bluemine.data.repo

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.module

val repoModule = module {
    single { Repository(get(), get(), get(), createPref("repo")) }
    single { SettingsRepository(get(), createPref("settings"), get()) }
}

private fun ModuleDefinition.createPref(prefName: String) =
    androidContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)