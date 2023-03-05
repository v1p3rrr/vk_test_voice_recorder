package com.vpr.vk_test_voice_recorder.data.repository

import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordEntity
import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordDao
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRecordRepositoryImpl @Inject constructor(
    private val dao: VoiceRecordDao
) : VoiceRecordRepository {

    override val allVoiceRecords: Flow<List<VoiceRecordEntity>> = dao.getAllVoiceRecords()

    override suspend fun insert(voiceRecord: VoiceRecordEntity) {
        dao.insertVoiceRecord(voiceRecord)
    }

    override suspend fun delete(voiceRecordId: Long) {
        dao.deleteVoiceRecordById(voiceRecordId)
    }

    override suspend fun updateName(id: Long, newName: String) {
        dao.updateVoiceRecordNameById(id, newName)
    }
}