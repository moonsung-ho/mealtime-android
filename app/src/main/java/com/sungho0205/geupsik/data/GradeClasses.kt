package com.sungho0205.geupsik.data

data class GradeClassInfoList(
    val classInfo: List<Map<String, Any>>
)

data class GradeClassInfoRow(
    val row: List<GradeClass>
)

data class GradeClass(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val AY: String,
    val GRADE: String,
    val DGHT_CRSE_SC_NM: String,
    val SCHUL_CRSE_SC_NM: String,
    val ORD_SC_NM: String?,
    val DDDEP_NM: String?,
    val CLASS_NM: String,
    val LOAD_DTM: String,
)
