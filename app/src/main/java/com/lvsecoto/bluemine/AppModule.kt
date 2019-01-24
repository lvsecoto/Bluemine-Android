package com.lvsecoto.bluemine

import com.lvsecoto.bluemine.ui.home.project.ProjectListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { ProjectListViewModel(get()) }
}
