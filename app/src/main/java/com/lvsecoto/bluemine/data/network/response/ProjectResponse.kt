package com.lvsecoto.bluemine.data.network.response

data class ProjectResponse(
    val projects: List<Project>
) {
    data class Project(
        val createdOn: String,
        val description: String,
        val id: Int,
        val identifier: String,
        val isPublic: Boolean,
        val name: String,
        val status: Int,
        val updatedOn: String
    )
}