package com.lvsecoto.bluemine.data.network.response

data class IssueDetailResponse(
    val issue: Issue = Issue()
) {
    data class Issue(
        val attachments: List<Attachment> = listOf(),
        val author: Author = Author(),
        val closed_on: Any = Any(),
        val created_on: String = "",
        val description: String = "",
        val done_ratio: Int = 0,
        val due_date: Any = Any(),
        val estimated_hours: Any = Any(),
        val id: Int = 0,
        val is_private: Boolean = false,
        val priority: Priority = Priority(),
        val project: Project = Project(),
        val spent_hours: Int = 0,
        val start_date: String = "",
        val status: Status = Status(),
        val subject: String = "",
        val total_estimated_hours: Any = Any(),
        val total_spent_hours: Int = 0,
        val tracker: Tracker = Tracker(),
        val updated_on: String = ""
    ) {
        data class Status(
            val id: Int = 0,
            val name: String = ""
        )

        data class Attachment(
            val author: Author = Author(),
            val content_type: String = "",
            val content_url: String = "",
            val created_on: String = "",
            val description: String = "",
            val filename: String = "",
            val filesize: Int = 0,
            val id: Int = 0,
            val thumbnail_url: String = ""
        ) {
            data class Author(
                val id: Int = 0,
                val name: String = ""
            )
        }

        data class Author(
            val id: Int = 0,
            val name: String = ""
        )

        data class Project(
            val id: Int = 0,
            val name: String = ""
        )

        data class Priority(
            val id: Int = 0,
            val name: String = ""
        )

        data class Tracker(
            val id: Int = 0,
            val name: String = ""
        )
    }
}