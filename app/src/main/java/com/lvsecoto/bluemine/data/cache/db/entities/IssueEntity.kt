package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "issue",
    indices = [
        Index(
            value = ["issueId"],
            unique = true
        ),
        Index(
            value = ["projectId"],
            unique = false
        )
    ]
)
data class IssueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val issueId: Int,
    val projectId: Int,

    val subject: String,
    val statusId: Int,
    val statusName: String,
    val meta: String
)