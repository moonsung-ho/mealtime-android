package com.sungho0205.geupsik.data

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.sungho0205.geupsik.service.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class SchoolInfoList(
    val schoolInfo: List<Map<String, Any>> // schoolInfo[0]: HeadList, schoolInfo[1]: RowList
)

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

data class Row(
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

private val api: SchoolAPI = APIClient().getClient().create(SchoolAPI::class.java)
private val gson = Gson()
private const val TAG: String = "API CALL"

fun searchSchools(query: String, result: SnapshotStateList<School>): Unit {
    val call = api.getSchools("영신")
    call.enqueue(object : Callback<SchoolInfoList> {
        override fun onResponse(
            call: Call<SchoolInfoList>, response: Response<SchoolInfoList>
        ) {
            val rawHead = response.body()?.schoolInfo?.get(0)
            val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
            val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
            val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
            val message = res?.MESSAGE
            val rawRows = response.body()?.schoolInfo?.get(1)
            val schools = gson.fromJson(gson.toJson(rawRows), Row::class.java).row

            Log.d(TAG, "성공 : $count $message")
            result.clear()
            result.addAll(schools)
        }

        override fun onFailure(call: Call<SchoolInfoList>, t: Throwable) {
            Log.d(TAG, "실패 : $t")
        }
    })
}