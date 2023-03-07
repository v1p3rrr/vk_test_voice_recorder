package com.vpr.vk_test_voice_recorder.utils

import android.content.Context
import android.provider.Settings.Global.getString
import com.vpr.vk_test_voice_recorder.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DATE_FORMAT = "dd.MM.yyyy"
private const val TIME_HOURS_MINUTES_FORMAT = "HH:mm"
private const val TIME_FORMAT = "HH:mm:ss"
private const val TIME_MINUTES_SECONDS_FORMAT = "mm:ss"

class DateTimeFormatter @Inject constructor(private val context: Context) {

    fun getDateTimePair(timestamp: Long): Pair<String, String> {
        val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp)
        val time = SimpleDateFormat(TIME_HOURS_MINUTES_FORMAT, Locale.getDefault()).format(timestamp)
        return date to time
    }

    fun getDate(timestamp: Long): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp)
    }

    fun getHoursMinutesTime(timestamp: Long): String {
        return SimpleDateFormat(TIME_HOURS_MINUTES_FORMAT, Locale.getDefault()).format(timestamp)
    }

    fun getDuration(timestamp: Long): String {
        var hours = TimeUnit.MILLISECONDS.toHours(timestamp)
        var minutes = TimeUnit.MILLISECONDS.toMinutes(timestamp - TimeUnit.HOURS.toMillis(hours))
        var seconds = TimeUnit.MILLISECONDS.toSeconds(timestamp - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes))

        if (timestamp <= 0){
            hours = 0
            minutes = 0
            seconds = 0
        }

        return if (hours >= 1) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun formatDateDeicticDay(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        calendar.timeInMillis = timestamp
        val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp)

        return when {
            isToday(now, timestamp) -> context.getString(R.string.today)
            isYesterday(now, timestamp) -> context.getString(R.string.yesterday)
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