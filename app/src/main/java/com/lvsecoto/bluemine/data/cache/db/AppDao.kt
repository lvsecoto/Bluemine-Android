package com.lvsecoto.bluemine.data.cache.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.*
import com.lvsecoto.bluemine.data.cache.db.entities.AttachmentEntity
import com.lvsecoto.bluemine.data.cache.db.entities.IssueDetailEntity
import com.lvsecoto.bluemine.data.cache.db.entities.IssueEntity
import com.lvsecoto.bluemine.data.cache.db.entities.ProjectEntity
import com.lvsecoto.bluemine.data.vo.Issue
import com.lvsecoto.bluemine.data.vo.IssueDetail
import com.lvsecoto.bluemine.data.vo.Project

fun createAppDao(db: AppDatabase) =
    db.appDao()

@Dao()
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttachments(attachmentEntities: List<AttachmentEntity>)

    @Query("DELETE FROM attachments WHERE issueId == :issueId")
    fun deleteAttachmentsByIssue(issueId: Int)

    @Transaction
    fun initAttachmentByIssue(issueId: Int, attachmentEntities: List<AttachmentEntity>) {
        deleteAttachmentsByIssue(issueId)
        insertAttachments(attachmentEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIssueDetail(issueDetailEntity: IssueDetailEntity)

    @Transaction
    fun initIssueDetailById(
        issueId: Int,
        issueDetailEntity: IssueDetailEntity,
        attachmentEntities: List<AttachmentEntity>
    ) {
        insertIssueDetail(issueDetailEntity)
        deleteAttachmentsByIssue(issueId)
        insertAttachments(attachmentEntities)
    }

    @Transaction
    @Query("SELECT * FROM issue WHERE issueId == :issueId")
    fun findIssueDetailRelationById(issueId: Int): LiveData<IssueDetailRelation>

    @Query("""UPDATE issue
        SET statusId = :statusId, statusName = :statusName
        WHERE issueId == :issueId
        """)
    fun updateIssueStatus(issueId: Int, statusId: Int, statusName: String)

    class IssueDetailRelation(
        @Embedded
        val issueEntity: IssueEntity,

        @Relation(
            entity = IssueDetailEntity::class,
            parentColumn = "issueId", entityColumn = "issueId"
        )
        val issueDetailEntities: List<IssueDetailEntity>,

        @Relation(
            entity = AttachmentEntity::class,
            parentColumn = "issueId", entityColumn = "issueId"
        )
        val attachmentEntities: List<AttachmentEntity>?
    ) {
        val issueDetailEntity: IssueDetailEntity?
            get() = issueDetailEntities.firstOrNull()
    }
}

fun AppDao.findIssueDetailById(issueId: Int) =
    this.findIssueDetailRelationById(issueId).map {
        IssueDetail(
            issueId = it.issueEntity.issueId,
            subject = it.issueEntity.subject,
            statusName = it.issueEntity.statusName,

            hasDetail = it.issueDetailEntity != null,

            description = it.issueDetailEntity?.description ?: "",
            priorityName = it.issueDetailEntity?.priorityName ?: "",
            updatedOn = it.issueDetailEntity?.updateOn ?: "",
            attachments = it.attachmentEntities?.map {
                IssueDetail.Attachment(
                    fileName = it.fileName,
                    attachmentId = it.attachmentId,
                    contentType = it.contentType,
                    contentUrl = it.contentUrl,
                    thumbnailUrl = it.thumbnailUrl
                )
            }?: emptyList()
        )
    }

private fun <I, O> LiveData<I>.switchMap(input: (I) -> LiveData<O>): LiveData<O> =
    Transformations.switchMap(this, input)

private fun <I, O> LiveData<I>.map(input: (I) -> O): LiveData<O> =
    Transformations.map(this, input)
