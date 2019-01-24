package com.lvsecoto.bluemine.ui.home.project

import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.Repository

class ProjectListViewModel(repo: Repository) : ViewModel() {
    val projects = repo.getProject()
}