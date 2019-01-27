package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IssueDetail(
    @PrimaryKey(autoGenerate = true)
    val int: Int,

    val issueId: Int

)
