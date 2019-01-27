package com.lvsecoto.bluemine.data.cache.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.db.entities.ProjectEntity
import com.lvsecoto.bluemine.data.vo.Project
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val dbModule = module {
    single { createAppDb(androidContext(), get()) }
    single { createAppDao(get()) }
}

fun createAppDb(context: Context, executors: AppExecutors) =
    Room
        .databaseBuilder(
            context,
            AppDataBase::class.java,
            "db")
        .setQueryExecutor(executors.diskIO())
        .build()

fun createAppDao(db: AppDataBase) =
    db.appDao()

@Database(
    entities = arrayOf(
        ProjectEntity::class
    ),
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun appDao() : AppDao
}

@Dao(

)
interface AppDao {

    @Insert
    fun insertProject(projectEntities: List<ProjectEntity>)

    @Query("DELETE from project WHERE 1==1")
    fun deleteAllProject()

    @Transaction
    fun initWithProjects(projectEntities: List<ProjectEntity>) {
        deleteAllProject()
        insertProject(projectEntities)
    }

    @Query("SELECT projectId as id, projectName as name FROM project")
    fun findAllProject() : LiveData<List<Project>>

}
