package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    indices = [
        Index(
            "issueId"
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = IssueEntity::class,
            parentColumns = ["issueId"],
            childColumns = ["issueId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val issueId: Int,
    val attachmentId: Int,
    val fileName: String,
    val contentType: String,
    val contentUrl: String,
    val thumbnailUrl: String
)