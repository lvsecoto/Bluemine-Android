package com.lvsecoto.bluemine

import com.lvsecoto.bluemine.ui.home.HomeViewModel
import com.lvsecoto.bluemine.ui.home.issues.IssuesViewModel
import com.lvsecoto.bluemine.ui.home.project.ProjectListViewModel
import com.lvsecoto.bluemine.ui.issuedetail.IssueDetailViewModel
import com.lvsecoto.bluemine.ui.settings.SettingsViewModel
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { AppExecutors() }

    viewModel<ProjectListViewModel>()
    viewModel { IssuesViewModel(get()) }
    viewModel<HomeViewModel>()
    viewModel<IssueDetailViewModel>()
    viewModel<SettingsViewModel>()
}
