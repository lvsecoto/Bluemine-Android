package com.lvsecoto.bluemine.data.vo

data class Issue(
    val id: Int,
    val subject: String,
    val statusId: Int,
    val statusName: String,
    val meta: String
)
