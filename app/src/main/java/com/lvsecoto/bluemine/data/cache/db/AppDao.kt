package com.lvsecoto.bluemine.data.cache.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.lvsecoto.bluemine.data.cache.db.entities.IssueEntity
import com.lvsecoto.bluemine.data.cache.db.entities.ProjectEntity
import com.lvsecoto.bluemine.data.vo.Issue
import com.lvsecoto.bluemine.data.vo.Project

fun createAppDao(db: AppDatabase) =
    db.appDao()

@Dao()
interface AppDao {

    @Insert
    fun insertProjects(projectEntities: List<ProjectEntity>)

    @Query("DELETE from project WHERE 1==1")
    fun deleteAllProjects()

    @Transaction
    fun initWithProjects(projectEntities: List<ProjectEntity>) {
        deleteAllProjects()
        insertProjects(projectEntities)
    }

    @Query("SELECT projectId as id, projectName as name FROM project ORDER BY projectId")
    fun findAllProjects(): LiveData<List<Project>>

    @Insert
    fun insertIssues(issues: List<IssueEntity>)

    @Query("DELETE from issue WHERE projectId == :projectId")
    fun deleteIssuesByProject(projectId: Int)

    @Transaction
    fun initWithIssuesByProject(issues: List<IssueEntity>, projectId: Int) {
        deleteIssuesByProject(projectId)
        insertIssues(issues)
    }

    @Query(
        """SELECT
            issueId as id,
            subject,
            statusId,
            statusName,
            meta
            FROM issue
            WHERE projectId == :projectId
            ORDER BY issueId
            """
    )
    fun findIssuesByProject(projectId: Int): LiveData<List<Issue>>

//    @Query("UPDATE OR REPLACE ")
//    fun insertIssueDetail()
}