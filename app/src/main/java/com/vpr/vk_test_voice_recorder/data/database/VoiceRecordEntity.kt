package com.vpr.vk_test_voice_recorder.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voice_records")
data class VoiceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filePath: String,
    val name: String,
    val duration: Long,
    val timestamp: Long,
)