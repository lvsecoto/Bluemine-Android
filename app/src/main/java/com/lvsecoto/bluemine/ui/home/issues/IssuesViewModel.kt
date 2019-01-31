package com.lvsecoto.bluemine.ui.home.issues

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.Repository
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.Issue
import com.lvsecoto.bluemine.utils.ActionLiveData

class IssuesViewModel(
    repo: Repository
) : ViewModel() {

    // todo 先获取issue处理方式，再处理
    private val searchIssueByProjectId = MutableLiveData<Int>()

    fun searchIssue(projectId: Int) {
        searchIssueByProjectId.value = projectId
    }

    val issues = Transformations.switchMap(searchIssueByProjectId) {
        repo.getIssues(it)
    }!!

    private val actionRefreshIssues =
        ActionLiveData.create<Int, Resource<List<Issue>>> {
            repo.getIssues(it, now = true)
        }

    fun refreshIssue() =
        searchIssueByProjectId.value?.let {
            actionRefreshIssues.input(it)
            true
        } ?: false

    fun getRefreshIssueResult() =
        actionRefreshIssues.asLiveData()

    enum class STATUS(val statusId: Int, val statusName: String) {
        CREATED(1, "新建"),
        WORKING(2, "进行中"),
        RESOLVED(3, "已解决")
    }

    private val actionChangeIssueStatus = ActionLiveData.create { issue: Issue ->

        val targetStatusId: Int
        val targetStatusName: String

        when {
           issue.statusId != STATUS.RESOLVED.statusId -> {
                targetStatusId = STATUS.RESOLVED.statusId
                targetStatusName = STATUS.RESOLVED.statusName
            }
            else -> {
                targetStatusId = STATUS.WORKING.statusId
                targetStatusName = STATUS.WORKING.statusName
            }
        }

        repo.changeIssueStatus(issue.id, targetStatusId, targetStatusName)
    }

    fun changeIssueStatus(issue: Issue) {
        actionChangeIssueStatus.input(issue)
    }

    val changeIssueStatusResult
        get() = actionChangeIssueStatus.asLiveData()
}