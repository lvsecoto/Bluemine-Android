package com.lvsecoto.bluemine.data.network.response

data class IssuesResponse(
    val issues: List<Issue>,
    val limit: Int,
    val offset: Int,
    val total_count: Int
) {
    data class Issue(
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
        val start_date: String,
        val status: Status,
        val subject: String,
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