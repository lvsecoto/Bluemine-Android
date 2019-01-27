package com.lvsecoto.bluemine.data.cache.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "project"
)
class ProjectEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectId: Int,
    val projectName: String
)
