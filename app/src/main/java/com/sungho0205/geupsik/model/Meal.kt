package com.sungho0205.geupsik.model

data class MealServiceDietInfoList(
    val mealServiceDietInfo: List<Map<String, Any>>
)

data class MealServiceDietInfoRow(
    val row: List<MealServiceDiet>
)

data class MealServiceDiet(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val MMEAL_SC_CODE: String,
    val MMEAL_SC_NM: String,
    val MLSV_YMD: String,
    val MLSV_FGR: Float,
    val DDISH_NM: String,
    val ORPLC_INFO: String,
    val CAL_INFO: String,
    val NTR_INFO: String,
    val MLSV_FROM_YMD: String,
    val MLSV_TO_YMD: String,
)
