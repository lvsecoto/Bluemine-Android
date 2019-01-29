package com.lvsecoto.bluemine.data.network.request

class IssueStatusRequest(statusId: Int) {
    val issue: Issue

    init {
        issue = Issue(statusId)
    }

    class Issue(val statusId: Int)
}
