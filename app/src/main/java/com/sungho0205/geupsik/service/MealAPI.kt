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

interface MealAPI {
    @GET("hub/mealServiceDietInfo")
    fun getMeal(
        @Query("ATPT_OFCDC_SC_CODE") atpt_ofcdc_sc_code: String,
        @Query("SD_SCHUL_CODE") sd_schul_code: String,
        @Query("MLSV_YMD") mlsvYmd: String,
        @Query("Type") type: String? = "json",
        @Query("pIndex") pIndex: String? = "1",
        @Query("pSize") pSize: String? = "100",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<MealServiceDietInfoList>
}

private val api: MealAPI = APIClient().getClient(API.OpenNeis).create(MealAPI::class.java)
private val gson = Gson()
private const val TAG: String = "MEAL API CALL"

fun queryMeal(
    atptOfcdcScCode: String,
    sdSchulCode: String,
    mlsvYmd: String,
    result: SnapshotStateList<MealServiceDiet>,
    progress: MutableState<Float>
) {
    timer(period = 100, initialDelay = 0) {
        progress.value += 0.3f
        if (progress.value >= 1f) {
            cancel()
            progress.value = 0f
        }
    }
    val call = api.getMeal(atptOfcdcScCode, sdSchulCode, mlsvYmd)
    call.enqueue(object : Callback<MealServiceDietInfoList> {
        override fun onResponse(
            call: Call<MealServiceDietInfoList>,
            response: Response<MealServiceDietInfoList>
        ) {
            try {
                val rawHead = response.body()?.mealServiceDietInfo?.get(0)
                val head = gson.fromJson(gson.toJson(rawHead), Head::class.java).head
                val count = gson.fromJson(gson.toJson(head[0]), Count::class.java).list_total_count
                val res = gson.fromJson(gson.toJson(head[1]), ResultInfo::class.java).RESULT
                val message = res?.MESSAGE
                val rawRows = response.body()?.mealServiceDietInfo?.get(1)
                val meals =
                    gson.fromJson(gson.toJson(rawRows), MealServiceDietInfoRow::class.java).row

                Log.d(TAG, "성공 : $count $message")
                result.clear()
                result.addAll(meals)
                progress.value = 0f
            } catch (t: Throwable) {
                Log.d(TAG, "실패 : $t")
                result.clear()
                progress.value = 0f
            }
        }

        override fun onFailure(call: Call<MealServiceDietInfoList>, t: Throwable) {
            Log.d(TAG, "실패 : $t")
            progress.value = 0f
        }
    })
}