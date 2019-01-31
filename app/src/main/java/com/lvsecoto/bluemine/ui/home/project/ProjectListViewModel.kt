package com.lvsecoto.bluemine.ui.home.project

import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.Repository
import com.lvsecoto.bluemine.utils.ActionLiveData

class ProjectListViewModel(repo: Repository) : ViewModel() {
    val projects = repo.getProjects()

    private val actionRefreshProject =
        ActionLiveData.create { _: Any? -> repo.getProjects(now = true) }

    fun refreshProject() {
        actionRefreshProject.call()
    }

    val refreshResult
        get() = actionRefreshProject.asLiveData()
}