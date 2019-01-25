package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.utils.objectListLiveData
import com.lvsecoto.bluemine.data.cache.utils.putObjectOrObjectList
import com.lvsecoto.bluemine.data.network.response.IssuesResponse
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.ApiResponse
import com.lvsecoto.bluemine.data.repo.utils.NetworkBoundResource
import com.lvsecoto.bluemine.data.repo.utils.RateLimiter
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.Issue
import com.lvsecoto.bluemine.data.vo.Project
import java.util.concurrent.TimeUnit

private const val KEY_PROJECTS = "KEY_PROJECTS"
private const val KEY_ISSUES = "KEY_ISSUES"

class Repository(
    private val executes: AppExecutors,
    private val service: RedMineService,
    private val pref: SharedPreferences
) {
    val limiter = RateLimiter<String>(5, TimeUnit.MINUTES)

    fun getProjects(): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, ProjectResponse>(executes) {
            override fun saveCallResult(item: ProjectResponse) {
                pref.putObjectOrObjectList(KEY_PROJECTS, item.projects.map {
                    Project(id = it.id, name = it.name)
                })
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                return data == null || data.isEmpty() || limiter.shouldFetch(KEY_PROJECTS)
            }

            override fun loadFromDb(): LiveData<List<Project>> =
                pref.objectListLiveData(KEY_PROJECTS)

            override fun createCall(): LiveData<ApiResponse<ProjectResponse>> =
                service.getProject()

        }.asLiveData()
    }

    fun getIssues(): LiveData<Resource<List<Issue>>> {
        return object : NetworkBoundResource<List<Issue>, IssuesResponse>(executes) {
            override fun saveCallResult(item: IssuesResponse) {
                pref.putObjectOrObjectList(KEY_ISSUES, item.issues.map {
                    Issue(it.id, it.subject, it.status.id, it.status.name, it.priority.name)
                })
            }

            override fun shouldFetch(data: List<Issue>?): Boolean =
                data == null || data.isEmpty() || limiter.shouldFetch(KEY_ISSUES)

            override fun loadFromDb(): LiveData<List<Issue>> =
                pref.objectListLiveData(KEY_ISSUES)

            override fun createCall(): LiveData<ApiResponse<IssuesResponse>> =
                service.getIssues()

        }.asLiveData()
    }
}