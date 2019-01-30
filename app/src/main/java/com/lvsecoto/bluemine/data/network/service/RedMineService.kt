package com.lvsecoto.bluemine.data.network.service

import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.data.network.request.IssueStatusRequest
import com.lvsecoto.bluemine.data.network.response.IssueDetailResponse
import com.lvsecoto.bluemine.data.network.response.IssuesResponse
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.utils.ApiResponse
import retrofit2.http.*

interface RedMineService {

    @GET("/projects.json")
    fun getProject(): LiveData<ApiResponse<ProjectResponse>>

    @GET("/issues.json")
    fun getIssues(@Query("project_id") projectId: Int): LiveData<ApiResponse<IssuesResponse>>

    @GET("/issues/{issue_id}.json?include=attachments")
    fun getIssueDetail(@Path("issue_id") id: Int): LiveData<ApiResponse<IssueDetailResponse>>

    @PUT("/issues/{issue_id}.json")
    fun updateIssueStatus(@Path("issue_id") issueId: Int, @Body issuesStatusRequest: IssueStatusRequest): LiveData<ApiResponse<Void>>
}