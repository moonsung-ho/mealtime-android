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

private val gson = Gson()

interface HighTimetableAPI {
    @GET("/hub/hisTimetable")
    fun getTimetable(
        @Query("ATPT_OFCDC_SC_CODE") atpt_ofcdc_sc_code: String,
        @Query("SD_SCHUL_CODE") sd_schul_code: String,
        @Query("ALL_TI_YMD") allTiYmd: String,
        @Query("GRADE") grade_: String,
        @Query("CLASS_NM") class_: String,
        @Query("type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<HisTimetableList>
}

private val apiHigh: HighTimetableAPI = APIClient().getClient(API.OpenNeis).create(HighTimetableAPI::class.java)
private const val TAG_HIGH: String = "TIMETABLE High school API CALL"

fun queryHighTimetable(
    atptOfcdcScCode: String,
    sdSchulCode: String,
    allTiYmd: String,
    grade_: String,
    class_: String,
    result: SnapshotStateList<HisTimetable>,
    progress: MutableState<Float>
) {
    timer(period = 100, initialDelay = 0) {
        progress.value += 0.3f
        if (progress.value >= 1f) {
            cancel()
            progress.value = 0f
        }
    }
    val call = apiHigh.getTimetable(atptOfcdcScCode, sdSchulCode, allTiYmd, grade_, class_)
    call.enqueue(object : Callback<HisTimetableList> {
        override fun onResponse(
            call: Call<HisTimetableList>, response: Response<HisTimetableList>
        ) {
            try {
                val rawHead = response.body()?.hisTimetable?.get(0)
                val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
                val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
                val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
                val message = res?.MESSAGE
                val rawRows = response.body()?.hisTimetable?.get(1)
                val timetables =
                    gson.fromJson(gson.toJson(rawRows), HisTimetableRow::class.java).row

                Log.d(TAG_HIGH, "성공 : $count $message")
                result.clear()
                result.addAll(timetables)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG_HIGH, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<HisTimetableList>, t: Throwable) {
            Log.d(TAG_HIGH, "실패 : $t")
            progress.value = 0f
        }
    })
}

interface MiddleTimetableAPI {
    @GET("/hub/misTimetable")
    fun getTimetable(
        @Query("ATPT_OFCDC_SC_CODE") atpt_ofcdc_sc_code: String,
        @Query("SD_SCHUL_CODE") sd_schul_code: String,
        @Query("ALL_TI_YMD") allTiYmd: String,
        @Query("GRADE") grade_: String,
        @Query("CLASS_NM") class_: String,
        @Query("type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<MisTimetableList>
}

private val apiMiddle: MiddleTimetableAPI = APIClient().getClient(API.OpenNeis).create(MiddleTimetableAPI::class.java)
private const val TAG_MIDDLE: String = "TIMETABLE Middle school API CALL"

fun queryMiddleTimetable(
    atptOfcdcScCode: String,
    sdSchulCode: String,
    allTiYmd: String,
    grade_: String,
    class_: String,
    result: SnapshotStateList<MisTimetable>,
    progress: MutableState<Float>
) {
    timer(period = 100, initialDelay = 0) {
        progress.value += 0.3f
        if (progress.value >= 1f) {
            cancel()
            progress.value = 0f
        }
    }
    val call = apiMiddle.getTimetable(atptOfcdcScCode, sdSchulCode, allTiYmd, grade_, class_)
    call.enqueue(object : Callback<MisTimetableList> {
        override fun onResponse(
            call: Call<MisTimetableList>, response: Response<MisTimetableList>
        ) {
            try {
                val rawHead = response.body()?.misTimetable?.get(0)
                val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
                val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
                val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
                val message = res?.MESSAGE
                val rawRows = response.body()?.misTimetable?.get(1)
                val timetables =
                    gson.fromJson(gson.toJson(rawRows), MisTimetableRow::class.java).row

                Log.d(TAG_MIDDLE, "성공 : $count $message")
                result.clear()
                result.addAll(timetables)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG_MIDDLE, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<MisTimetableList>, t: Throwable) {
            Log.d(TAG_MIDDLE, "실패 : $t")
            progress.value = 0f
        }
    })
}

interface ElimentaryTimetableAPI {
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

private val apiElementary: ElimentaryTimetableAPI = APIClient().getClient(API.OpenNeis).create(ElimentaryTimetableAPI::class.java)
private const val TAG_ELIMENTARY: String = "TIMETABLE elementary school API CALL"

fun queryElimentaryTimetable(
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
    val call = apiElementary.getTimetable(atptOfcdcScCode, sdSchulCode, allTiYmd, grade_, class_)
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

                Log.d(TAG_ELIMENTARY, "성공 : $count $message")
                result.clear()
                result.addAll(timetables)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG_ELIMENTARY, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<ElsTimetableList>, t: Throwable) {
            Log.d(TAG_ELIMENTARY, "실패 : $t")
            progress.value = 0f
        }
    })
}

interface SpecialTimetableAPI {
    @GET("/hub/spsTimetable")
    fun getTimetable(
        @Query("ATPT_OFCDC_SC_CODE") atpt_ofcdc_sc_code: String,
        @Query("SD_SCHUL_CODE") sd_schul_code: String,
        @Query("ALL_TI_YMD") allTiYmd: String,
        @Query("GRADE") grade_: String,
        @Query("CLASS_NM") class_: String,
        @Query("type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<SpsTimetableList>
}

private val apiSpecial: SpecialTimetableAPI = APIClient().getClient(API.OpenNeis).create(SpecialTimetableAPI::class.java)
private const val TAG_SPECIAL: String = "TIMETABLE special school API CALL"

fun querySpecialTimetable(
    atptOfcdcScCode: String,
    sdSchulCode: String,
    allTiYmd: String,
    grade_: String,
    class_: String,
    result: SnapshotStateList<SpsTimetable>,
    progress: MutableState<Float>
) {
    timer(period = 100, initialDelay = 0) {
        progress.value += 0.3f
        if (progress.value >= 1f) {
            cancel()
            progress.value = 0f
        }
    }
    val call = apiSpecial.getTimetable(atptOfcdcScCode, sdSchulCode, allTiYmd, grade_, class_)
    call.enqueue(object : Callback<SpsTimetableList> {
        override fun onResponse(
            call: Call<SpsTimetableList>, response: Response<SpsTimetableList>
        ) {
            try {
                val rawHead = response.body()?.spsTimetable?.get(0)
                val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
                val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
                val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
                val message = res?.MESSAGE
                val rawRows = response.body()?.spsTimetable?.get(1)
                val timetables =
                    gson.fromJson(gson.toJson(rawRows), SpsTimetableRow::class.java).row

                Log.d(TAG_SPECIAL, "성공 : $count $message")
                result.clear()
                result.addAll(timetables)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG_SPECIAL, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<SpsTimetableList>, t: Throwable) {
            Log.d(TAG_SPECIAL, "실패 : $t")
            progress.value = 0f
        }
    })
}