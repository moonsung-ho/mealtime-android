package com.sungho0205.geupsik.service

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.sungho0205.geupsik.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GradeClassAPI {
    @GET("/hub/classInfo")
    fun getGradeClasses(
        @Query("ATPT_OFCDC_SC_CODE") atpt_ofcdc_sc_code: String,
        @Query("SD_SCHUL_CODE") sd_schul_code: String,
        @Query("AY") ay: String? = "2023",
        @Query("type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<GradeClassInfoList>
}

private val api: GradeClassAPI = APIClient().getClient().create(GradeClassAPI::class.java)
private val gson = Gson()
private const val TAG: String = "GRADECLASS API CALL"

fun searchGradeClasses(atptOfcdcScCode: String, sdSchulCode: String, result: SnapshotStateList<GradeClass>) {
    val call = api.getGradeClasses(atptOfcdcScCode, sdSchulCode)
    call.enqueue(object : Callback<GradeClassInfoList> {
        override fun onResponse(
            call: Call<GradeClassInfoList>,
            response: Response<GradeClassInfoList>
        ) {
            val rawHead = response.body()?.classInfo?.get(0)
            val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
            val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
            val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
            val message = res?.MESSAGE
            val rawRows = response.body()?.classInfo?.get(1)
            val gradeClasses = gson.fromJson(gson.toJson(rawRows), GradeClassInfoRow::class.java).row

            Log.d(TAG, "성공 : $count $message")
            result.clear()
            result.addAll(gradeClasses)
        }

        override fun onFailure(call: Call<GradeClassInfoList>, t: Throwable) {
            Log.d(TAG, "실패 : $t")
        }
    })
}