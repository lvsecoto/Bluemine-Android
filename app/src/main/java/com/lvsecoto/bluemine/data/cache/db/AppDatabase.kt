package com.lvsecoto.bluemine.data.cache.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.db.entities.AttachmentEntity
import com.lvsecoto.bluemine.data.cache.db.entities.IssueDetailEntity
import com.lvsecoto.bluemine.data.cache.db.entities.IssueEntity
import com.lvsecoto.bluemine.data.cache.db.entities.ProjectEntity

fun createAppDatabase(context: Context, executors: AppExecutors) =
    Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "db"
    )
        .setQueryExecutor(executors.diskIO())
        .build()

@Database(
    entities = [
        ProjectEntity::class,
        IssueEntity::class,
        IssueDetailEntity::class,
        AttachmentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}