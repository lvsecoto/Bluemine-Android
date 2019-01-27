package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.db.AppDao
import com.lvsecoto.bluemine.data.cache.db.entities.IssueEntity
import com.lvsecoto.bluemine.data.cache.db.entities.ProjectEntity
import com.lvsecoto.bluemine.data.cache.utils.objectLiveData
import com.lvsecoto.bluemine.data.cache.utils.putObjectOrObjectList
import com.lvsecoto.bluemine.data.network.response.IssueDetailResponse
import com.lvsecoto.bluemine.data.network.response.IssuesResponse
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.ApiResponse
import com.lvsecoto.bluemine.data.repo.utils.NetworkBoundResource
import com.lvsecoto.bluemine.data.repo.utils.RateLimiter
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.Issue
import com.lvsecoto.bluemine.data.vo.IssueDetail
import com.lvsecoto.bluemine.data.vo.Project
import java.util.concurrent.TimeUnit

private const val KEY_PROJECTS = "KEY_PROJECTS"
private const val KEY_ISSUES = "KEY_ISSUES"
private const val KEY_ISSUES_DETAIL = "KEY_ISSUES_DETAIL"

class Repository(
    private val executes: AppExecutors,
    private val service: RedMineService,
    private val appDao: AppDao,
    private val pref: SharedPreferences
) {
    val limiter = RateLimiter<String>(5, TimeUnit.MINUTES)

    fun getProjects(): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, ProjectResponse>(executes) {
            override fun saveCallResult(item: ProjectResponse) {
                appDao.initWithProjects(
                    item.projects.map {
                        ProjectEntity(
                            projectId = it.id,
                            projectName = it.name
                        )
                    }
                )
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                return data == null || data.isEmpty() || limiter.shouldFetch(KEY_PROJECTS)
            }

            override fun loadFromDb(): LiveData<List<Project>> =
                appDao.findAllProjects()

            override fun createCall(): LiveData<ApiResponse<ProjectResponse>> =
                service.getProject()

        }.asLiveData()
    }

    fun getIssues(projectId: Int): LiveData<Resource<List<Issue>>> {
        val key = "${KEY_ISSUES}_$projectId"

        return object : NetworkBoundResource<List<Issue>, IssuesResponse>(executes) {
            override fun saveCallResult(item: IssuesResponse) {
                appDao.initWithIssuesByProject(
                    item.issues.map {
                        IssueEntity(
                            issueId = it.id,
                            projectId = projectId,
                            subject = it.subject,
                            statusId = it.status.id,
                            statusName = it.status.name,
                            meta = it.priority.name
                        )
                    },
                    projectId
                )
            }

            override fun shouldFetch(data: List<Issue>?): Boolean =
                data == null || data.isEmpty() || limiter.shouldFetch(key)

            override fun loadFromDb(): LiveData<List<Issue>> =
                appDao.findIssuesByProject(projectId)

            override fun createCall(): LiveData<ApiResponse<IssuesResponse>> =
                service.getIssues(projectId)

        }.asLiveData()
    }

    fun getIssueDetail(id: Int): LiveData<Resource<IssueDetail>> {
        val key = "${KEY_ISSUES_DETAIL}_$id"

        return object : NetworkBoundResource<IssueDetail, IssueDetailResponse>(executes) {
            override fun saveCallResult(item: IssueDetailResponse) {
                pref.putObjectOrObjectList(key, item.issue.let {
                    IssueDetail(
                        subject = it.subject,
                        description = it.description,
                        priorityName = it.priority.name,
                        statusName = it.status.name,
                        updatedOn = it.updated_on
                    )
                })
            }

            override fun shouldFetch(data: IssueDetail?): Boolean =
                data == null

            override fun loadFromDb(): LiveData<IssueDetail> =
                pref.objectLiveData(key)

            override fun createCall(): LiveData<ApiResponse<IssueDetailResponse>> =
                service.getIssueDetail(id)
        }.asLiveData()
    }

}