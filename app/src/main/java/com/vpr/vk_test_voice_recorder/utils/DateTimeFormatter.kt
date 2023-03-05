package com.vpr.vk_test_voice_recorder.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter {
    private val DATE_FORMAT = "dd.MM.yyyy"
    private val TIME_FORMAT = "HH:mm"
    private val TIME_WITH_SECONDS_FORMAT = "HH:mm:ss"

    fun getDateTimePair(timestamp: Long): Pair<String, String> {
        val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp)
        val time = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timestamp)
        return date to time
    }

    fun getDate(timestamp: Long): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp)
    }

    fun getTime(timestamp: Long): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timestamp)
    }
    
    //todo fix +3 hrs
    fun getTimeWithDaysAsHours(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val duration = calendar.timeInMillis - Calendar.getInstance().apply {
            timeInMillis = 0
        }.timeInMillis
        return SimpleDateFormat(TIME_WITH_SECONDS_FORMAT, Locale.getDefault()).format(Date(duration))
    }

    fun formatDateDeicticDay(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        calendar.timeInMillis = timestamp
        val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp)

        return when {
            isToday(now, timestamp) -> "Сегодня"
            isYesterday(now, timestamp) -> "Завтра" // todo add to resources
            else -> date
        }
    }

    private fun isToday(now: Long, timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        val nowDay = calendar.get(Calendar.DAY_OF_YEAR)
        calendar.timeInMillis = timestamp
        val day = calendar.get(Calendar.DAY_OF_YEAR)
        return nowDay == day
    }

    private fun isYesterday(now: Long, timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        val nowDay = calendar.get(Calendar.DAY_OF_YEAR)
        calendar.timeInMillis = timestamp
        val day = calendar.get(Calendar.DAY_OF_YEAR)
        return nowDay - day == 1
    }
}