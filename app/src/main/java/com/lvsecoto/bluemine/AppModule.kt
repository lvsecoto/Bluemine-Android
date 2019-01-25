package com.lvsecoto.bluemine

import com.lvsecoto.bluemine.ui.home.issues.IssuesViewModel
import com.lvsecoto.bluemine.ui.home.project.ProjectListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { AppExecutors() }

    viewModel { ProjectListViewModel(get()) }
    viewModel { IssuesViewModel(get()) }
}
