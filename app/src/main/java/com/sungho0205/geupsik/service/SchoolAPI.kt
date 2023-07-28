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

interface SchoolAPI {
    @GET("/hub/schoolInfo")
    fun getSchools(
        @Query("SCHUL_NM") name: String,
        @Query("LCTN_SC_NM") region: String?,
        @Query("Type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<SchoolInfoList>
}

private val api: SchoolAPI = APIClient().getClient(API.OpenNeis).create(SchoolAPI::class.java)
private val gson = Gson()
private const val TAG: String = " SCHOOL API CALL"

fun searchSchools(
    query: String,
    region: String?,
    result: SnapshotStateList<School>,
    progress: MutableState<Float>
): Unit {
    timer(period = 100, initialDelay = 0) {
        progress.value += 0.3f
        if (progress.value >= 1f) {
            cancel()
            progress.value = 0f
        }
    }
    val call = api.getSchools(query, region)
    call.enqueue(object : Callback<SchoolInfoList> {
        override fun onResponse(
            call: Call<SchoolInfoList>, response: Response<SchoolInfoList>
        ) {
            try {
                val rawHead = response.body()?.schoolInfo?.get(0)
                val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
                val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
                val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
                val message = res?.MESSAGE
                val rawRows = response.body()?.schoolInfo?.get(1)
                val schools = gson.fromJson(gson.toJson(rawRows), SchoolInfoRow::class.java).row

                Log.d(TAG, "성공 : $count $message")
                result.clear()
                result.addAll(schools)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<SchoolInfoList>, t: Throwable) {
            Log.d(TAG, "실패 : $t")
            progress.value = 0f
        }
    })
}