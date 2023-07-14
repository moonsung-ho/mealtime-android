package com.sungho0205.geupsik.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolAPI {
    @GET("/hub/schoolInfo")
    fun getSchools(
        @Query("SCHUL_NM") name: String,
        @Query("Type") type: String? = "json",
        @Query("KEY") key: String? = "a9a5367947564a1aa13e46ba545de634"
    ): Call<SchoolInfoList>
}