package com.lvsecoto.bluemine.ui.home

import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.utils.event.SingleLiveEvent
import com.lvsecoto.bluemine.data.vo.Project

class HomeViewModel : ViewModel() {
    val onClickProject = SingleLiveEvent<Project>()
    val onSelectProject = SingleLiveEvent<Project>()
}