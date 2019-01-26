package com.lvsecoto.bluemine.ui.home.issues

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.Repository

class IssuesViewModel(
    repo: Repository
) : ViewModel() {

    private val searchedProjectId = MutableLiveData<Int>()

    val issues = Transformations.switchMap(searchedProjectId) {
        repo.getIssues(it)
    }!!

    fun searchIssue(projectId: Int) {
        searchedProjectId.value = projectId
    }
}