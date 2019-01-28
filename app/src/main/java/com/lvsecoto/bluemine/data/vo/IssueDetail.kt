package com.lvsecoto.bluemine.data.vo

data class IssueDetail(
    val issueId: Int,
    val subject: String,
    val description: String,
    val priorityName: String,
    val statusName: String,
    val updatedOn: String,
    val hasDetail: Boolean
)
