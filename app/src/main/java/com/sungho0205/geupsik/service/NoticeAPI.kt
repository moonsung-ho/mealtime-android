package com.sungho0205.geupsik.service

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sungho0205.geupsik.model.Notice
import com.sungho0205.geupsik.model.NoticeList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface NoticeAPI {
    @GET("/moonsung-ho/mealtime-android/main/notices/notices.json")
    fun getNotices(): Call<NoticeList>
}

private val api: NoticeAPI = APIClient().getClient(API.GitHubs).create(NoticeAPI::class.java)
private const val TAG: String = "NOTICE API CALL"

fun queryNotices(
    result: SnapshotStateList<Notice>,
) {
    val call = api.getNotices()
    call.enqueue(object: Callback<NoticeList> {
        override fun onResponse(call: Call<NoticeList>, response: Response<NoticeList>) {
            try {
                val notices : List<Notice>? = response.body()?.data
                Log.d(TAG, "성공 ${notices.toString()}")
                result.clear()
                if (notices != null) {
                    result.addAll(notices)
                } else {
                    Log.d(TAG, "공지사항을 찾을 수 없음")
                }
            } catch (t : Throwable) {
                Log.d(TAG, "실패 : $t")
                result.clear()
            }
        }

        override fun onFailure(call: Call<NoticeList>, t: Throwable) {
            Log.d(TAG, "실패 : $t")
        }
    })
}