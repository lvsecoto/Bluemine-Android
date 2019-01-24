package com.lvsecoto.bluemine.data.repo

import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.ApiResponse

class Repository(
    private val executes: AppExecutors,
    private val service: RedMineService
) {
    fun getProject(): LiveData<ApiResponse<ProjectResponse>> {
        return service.getProject()
    }
}