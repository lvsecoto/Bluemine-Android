package com.lvsecoto.bluemine.data.network.service

import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.data.network.response.IssuesResponse
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.utils.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RedMineService {

    @GET("/projects.json")
    fun getProject(): LiveData<ApiResponse<ProjectResponse>>

    @GET("/issues.json")
    fun getIssues(@Query("project_id") projectId: Int): LiveData<ApiResponse<IssuesResponse>>
}