package com.sungho0205.geupsik.model

data class SchoolInfoList(
    val schoolInfo: List<Map<String, Any>> // schoolInfo[0]: HeadList, schoolInfo[1]: RowList
)

data class SchoolInfoRow(
    val row: List<School>
)

data class School(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val ENG_SCHUL_NM: String,
    val SCHUL_KND_SC_NM: String,
    val LCTN_SC_NM: String,
    val JU_ORG_NM: String,
    val FOND_SC_NM: String,
    val ORG_RDNZC: String,
    val ORG_RDNMA: String,
    val ORG_RDNDA: String,
    val ORG_TELNO: String,
    val HMPG_ADRES: String,
    val COEDU_SC_NM: String,
    val ORG_FAXNO: String,
    val HS_SC_NM: String?,
    val INDST_SPECL_CCCCL_EXST_YN: String,
    val HS_GNRL_BUSNS_SC_NM: String,
    val SPCLY_PURPS_HS_ORD_NM: String?,
    val ENE_BFE_SEHF_SC_NM: String,
    val DGHT_SC_NM: String,
    val FOND_YMD: String,
    val FOAS_MEMRD: String,
    val LOAD_DTM: String
)
