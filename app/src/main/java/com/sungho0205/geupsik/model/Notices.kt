package com.sungho0205.geupsik.model

data class NoticeList(
    val data: List<Notice>
)

data class Notice(
    val id: Int,
    val importance: String,
    val title: String,
    val description: String,
    val createdAt: String,
)
