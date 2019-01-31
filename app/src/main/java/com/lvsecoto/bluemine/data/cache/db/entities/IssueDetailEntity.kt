package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "issueDetail",
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
data class IssueDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val issueId: Int,

    val description: String,
    val priorityName: String,
    val updateOn: String
)
