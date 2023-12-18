package com.sungho0205.geupsik.model

// 고등학교
data class HisTimetableList(
    val hisTimetable: List<Map<String, Any>>
)

data class HisTimetableRow(
    val row: List<HisTimetable>
)

data class HisTimetable(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val AY: String,
    val SEM: String,
    val ALL_TI_YMD: String,
    val DGHT_CRSE_SC_NM: String,
    val ORD_SC_NM: String,
    val DDDEP_NM: String,
    val GRADE: String,
    val CLRM_NM: String,
    val CLASS_NM: String,
    val PERIO: String,
    val ITRT_CNTNT: String,
    val LOAD_DTM: String,
)

// 중학교
data class MisTimetableList(
    val misTimetable: List<Map<String, Any>>
)

data class MisTimetableRow(
    val row: List<MisTimetable>
)

data class MisTimetable(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val AY: String,
    val SEM: String,
    val ALL_TI_YMD: String,
    val DGHT_CRSE_SC_NM: String,
    val GRADE: String,
    val CLASS_NM: String,
    val PERIO: String,
    val ITRT_CNTNT: String,
    val LOAD_DTM: String,
)

// 초등학교
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

// 특수학교
data class SpsTimetableList(
    val spsTimetable: List<Map<String, Any>>
)

data class SpsTimetableRow(
    val row: List<SpsTimetable>
)

data class SpsTimetable(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val AY: String,
    val SEM: String,
    val ALL_TI_YMD: String,
    val SCHUL_CRSE_SC_NM: String,
    val GRADE: String,
    val CLRM_NM: String,
    val CLASS_NM: String,
    val PERIO: String,
    val ITRT_CNTNT: String,
    val LOAD_DTM: String,
)