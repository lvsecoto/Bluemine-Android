package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "project",
    indices = [
        Index(
            value = ["projectId"],
            unique = true
        )
    ]
)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectId: Int,
    val projectName: String
)
