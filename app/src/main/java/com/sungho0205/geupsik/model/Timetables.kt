package com.sungho0205.geupsik.model

data class ElsTimetableList(
    val elsTimetable: List<Map<String, Any>>
)

data class ElsTimetableRow(
    val row: List<ElsTimetable>
)

data class ElsTimetable(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val AY: String,
    val SEM: String,
    val ALL_TI_YMD: String,
    val GRADE: String,
    val CLASS_NM: String,
    val PERIO: String,
    val ITRT_CNTNT: String,
    val LOAD_DTM: String,
)
