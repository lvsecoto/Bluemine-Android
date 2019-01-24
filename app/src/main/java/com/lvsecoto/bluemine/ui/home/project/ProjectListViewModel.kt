package com.lvsecoto.bluemine.ui.home.project

import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.network.service.RedMineService

class ProjectListViewModel(redMineService: RedMineService) : ViewModel() {
    val projects = redMineService.getProject()
}