package com.lvsecoto.bluemine.data.vo

data class IssueDetail(
    val issueId: Int,
    val subject: String,
    val description: String,
    val priorityName: String,
    val statusName: String,
    val updatedOn: String,
    val hasDetail: Boolean,
    val attachments: List<Attachment>
) {
    data class Attachment(
        val fileName: String,
        val attachmentId: Int,
        val contentType: String,
        val contentUrl: String,
        val thumbnailUrl: String
    )
}
