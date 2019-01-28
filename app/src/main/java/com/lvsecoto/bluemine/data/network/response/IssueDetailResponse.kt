package com.lvsecoto.bluemine.data.network.response

data class IssueDetailResponse(
    val issue: Issue
) {
    data class Issue(
        val attachments: List<Attachment>?,
        val author: Author,
        val closed_on: Any,
        val created_on: String,
        val description: String,
        val done_ratio: Int,
        val due_date: Any,
        val estimated_hours: Any,
        val id: Int,
        val is_private: Boolean,
        val priority: Priority,
        val project: Project,
        val spent_hours: Int,
        val start_date: String,
        val status: Status,
        val subject: String,
        val total_estimated_hours: Any,
        val total_spent_hours: Int,
        val tracker: Tracker,
        val updated_on: String
    ) {
        data class Author(
            val id: Int,
            val name: String
        )

        data class Priority(
            val id: Int,
            val name: String
        )

        data class Status(
            val id: Int,
            val name: String
        )

        data class Attachment(
            val author: Author,
            val content_type: String,
            val content_url: String,
            val created_on: String,
            val description: String,
            val filename: String,
            val filesize: Int,
            val id: Int,
            val thumbnail_url: String
        ) {
            data class Author(
                val id: Int,
                val name: String
            )
        }

        data class Project(
            val id: Int,
            val name: String
        )

        data class Tracker(
            val id: Int,
            val name: String
        )
    }
}