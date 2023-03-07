package com.vpr.vk_test_voice_recorder.domain.repository

import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordEntity
import kotlinx.coroutines.flow.Flow

interface VoiceRecordRepository {
    val allVoiceRecords: Flow<List<VoiceRecordEntity>>

    suspend fun insert(voiceRecord: VoiceRecordEntity)

    suspend fun delete(voiceRecordId: Long)

    suspend fun updateNameAndFilePath(id: Long, newName: String, filePath: String)
}