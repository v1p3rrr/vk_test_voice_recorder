package com.vpr.vk_test_voice_recorder.utils

private const val SECONDS_IN_MINUTE = 60
private const val SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60
private const val SECONDS_IN_DAY = SECONDS_IN_HOUR * 24

class TimeDifferenceCalculator {
    fun getDurationString(startTime: Long, endTime: Long): String {
        val diff = endTime - startTime
        val hours = diff / SECONDS_IN_HOUR
        val minutes = (diff % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
        val seconds = diff % SECONDS_IN_MINUTE
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}