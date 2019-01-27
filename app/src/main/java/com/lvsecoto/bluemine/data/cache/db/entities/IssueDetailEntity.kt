package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "issueDetail"
)
data class IssueDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val issueId: Int

)
