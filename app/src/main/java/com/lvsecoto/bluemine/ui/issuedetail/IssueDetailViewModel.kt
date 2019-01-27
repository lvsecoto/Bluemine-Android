package com.lvsecoto.bluemine.ui.issuedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.Repository

class IssueDetailViewModel(
    private val repo: Repository
) : ViewModel() {
    val issueId = MutableLiveData<Int>()
    val issueDetail = Transformations.switchMap(issueId) {
        repo.getIssueDetail(it)
    }
}
