package com.sungho0205.geupsik.model

data class Head(
    val head: List<Map<String, Any>> // head[0]: Count, head[1]: ResultInfo
)

data class Count(
    val list_total_count: Int?
)

data class ResultInfo(
    val RESULT: Result?
)

data class Result(
    val CODE: String,
    val MESSAGE: String
)