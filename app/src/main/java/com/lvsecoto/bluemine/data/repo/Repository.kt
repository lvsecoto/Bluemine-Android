package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.db.AppDao
import com.lvsecoto.bluemine.data.cache.db.entities.AttachmentEntity
import com.lvsecoto.bluemine.data.cache.db.entities.IssueDetailEntity
import com.lvsecoto.bluemine.data.cache.db.entities.IssueEntity
import com.lvsecoto.bluemine.data.cache.db.entities.ProjectEntity
import com.lvsecoto.bluemine.data.cache.db.findIssueDetailById
import com.lvsecoto.bluemine.data.network.request.IssueStatusRequest
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

    fun getProjects(now: Boolean = false): LiveData<Resource<List<Project>>> {
        if (now) limiter.reset(KEY_PROJECTS)

        return object : NetworkBoundResource<List<Project>, ProjectResponse>(executes) {
            override fun saveCallResult(item: ProjectResponse?) {
                appDao.initProjectsAndClearIssues(
                    item!!.projects.map {
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

    fun getIssues(projectId: Int, now: Boolean = false): LiveData<Resource<List<Issue>>> {
        val key = "${KEY_ISSUES}_$projectId"
        if (now) limiter.reset(key)

        return object : NetworkBoundResource<List<Issue>, IssuesResponse>(executes) {
            override fun saveCallResult(item: IssuesResponse?) {
                appDao.initIssuesByProject(
                    item!!.issues.map {
                        IssueEntity(
                            issueId = it.id,
                            projectId = it.project.id,
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

    fun getIssueDetail(issueId: Int): LiveData<Resource<IssueDetail>> {
        return object : NetworkBoundResource<IssueDetail, IssueDetailResponse>(executes) {
            override fun saveCallResult(item: IssueDetailResponse?) {
                appDao.initIssueDetailByIssue(
                    item!!.issue.let {
                        IssueDetailEntity(
                            issueId = issueId,
                            description = it.description,
                            priorityName = it.priority.name,
                            updateOn = it.updated_on
                        )
                    },
                    item.issue.attachments?.map {
                        AttachmentEntity(
                            issueId = issueId,
                            attachmentId = it.id,
                            fileName = it.filename,
                            contentType = it.content_type,
                            contentUrl = it.content_url,
                            thumbnailUrl = it.thumbnail_url
                        )
                    }
                )
            }

            override fun shouldFetch(data: IssueDetail?): Boolean =
                data == null || !data.hasDetail

            override fun loadFromDb(): LiveData<IssueDetail> =
                appDao.findIssueDetailById(issueId)

            override fun createCall(): LiveData<ApiResponse<IssueDetailResponse>> =
                service.getIssueDetail(issueId)
        }.asLiveData()
    }

    fun changeIssueStatus(issueId: Int, statusId: Int, statusName: String): LiveData<Resource<Void>> {
        val result = MutableLiveData<Void>()
        result.value = null

        return object : NetworkBoundResource<Void, Void>(executes) {
            override fun saveCallResult(item: Void?) {
                appDao.updateIssueStatus(issueId, statusId, statusName)
            }

            override fun shouldFetch(data: Void?): Boolean =
                true

            override fun loadFromDb(): LiveData<Void> =
                result

            override fun createCall(): LiveData<ApiResponse<Void>> =
                service.updateIssueStatus(issueId, IssueStatusRequest(statusId))

        }.asLiveData()
    }
}