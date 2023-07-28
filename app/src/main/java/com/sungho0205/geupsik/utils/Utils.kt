package com.sungho0205.geupsik.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun compareDateAndTimestamp(dateString1: String, timestampString2: String): Int {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC") // ISO 8601 is in UTC
    val date1 = format.parse(dateString1)
    val timestamp2 = timestampString2.toLongOrNull()

    // dateString1과 timestamp2가 null이 아닐 경우, 날짜를 비교
    if (date1 != null && timestamp2 != null) {
        val timestamp1 = date1.time

        // timestamp1이 더 크면 1, 같으면 0, 작으면 -1 반환
        return when {
            timestamp1 > timestamp2 -> 1
            timestamp1 == timestamp2 -> 0
            else -> -1
        }
    }
    // dateString1이 null인 경우, 적절한 예외 처리
    throw IllegalArgumentException("Invalid date string")
}