package com.vpr.vk_test_voice_recorder.domain.model

data class VoiceRecord(
    val id: Long,
    val filePath: String,
    val name: String,
    val duration: String,
    val date: String,
    val time: String,
    val timestamp: Long
)