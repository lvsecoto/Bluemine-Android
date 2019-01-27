package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "issue",
    indices = [
        Index(
            value = ["issueId", "projectId"],
            unique = true
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["projectId"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
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