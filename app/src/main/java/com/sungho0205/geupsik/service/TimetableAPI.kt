package com.sungho0205.geupsik.service

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import com.sungho0205.geupsik.data.*
import com.sungho0205.geupsik.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.concurrent.timer

interface TimetableAPI {
    @GET("/hub/elsTimetable")
    fun getTimetable(
        @Query("ATPT_OFCDC_SC_CODE") atpt_ofcdc_sc_code: String,
        @Query("SD_SCHUL_CODE") sd_schul_code: String,
        @Query("ALL_TI_YMD") allTiYmd: String,
        @Query("GRADE") grade_: String,
        @Query("CLASS_NM") class_: String,
        @Query("type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<ElsTimetableList>
}

private val api: TimetableAPI = APIClient().getClient(API.OpenNeis).create(TimetableAPI::class.java)
private val gson = Gson()
private const val TAG: String = "TIMETABLE API CALL"

fun queryTimetable(
    atptOfcdcScCode: String,
    sdSchulCode: String,
    allTiYmd: String,
    grade_: String,
    class_: String,
    result: SnapshotStateList<ElsTimetable>,
    progress: MutableState<Float>
) {
    timer(period = 100, initialDelay = 0) {
        progress.value += 0.3f
        if (progress.value >= 1f) {
            cancel()
            progress.value = 0f
        }
    }
    val call = api.getTimetable(atptOfcdcScCode, sdSchulCode, allTiYmd, grade_, class_)
    call.enqueue(object : Callback<ElsTimetableList> {
        override fun onResponse(
            call: Call<ElsTimetableList>, response: Response<ElsTimetableList>
        ) {
            try {
                val rawHead = response.body()?.elsTimetable?.get(0)
                val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
                val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
                val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
                val message = res?.MESSAGE
                val rawRows = response.body()?.elsTimetable?.get(1)
                val timetables =
                    gson.fromJson(gson.toJson(rawRows), ElsTimetableRow::class.java).row

                Log.d(TAG, "성공 : $count $message")
                result.clear()
                result.addAll(timetables)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<ElsTimetableList>, t: Throwable) {
            Log.d(TAG, "실패 : $t")
            progress.value = 0f
        }
    })
}