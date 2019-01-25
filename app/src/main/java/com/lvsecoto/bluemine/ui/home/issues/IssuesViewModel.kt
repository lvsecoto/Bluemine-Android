package com.lvsecoto.bluemine.ui.home.issues

import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.Repository

class IssuesViewModel(
    repo: Repository
) : ViewModel() {
    val issues = repo.getIssues()
}