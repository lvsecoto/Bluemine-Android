package com.lvsecoto.bluemine.data.repo

import org.koin.dsl.module.module

val repoModule = module {
    single { Repository(get(), get()) }
}