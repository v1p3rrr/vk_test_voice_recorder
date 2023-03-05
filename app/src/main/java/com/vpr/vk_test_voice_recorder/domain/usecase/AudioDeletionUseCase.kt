package com.vpr.vk_test_voice_recorder.domain.usecase

import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.saver.FileManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioDeletionUseCase @Inject constructor(
    private val fileManager: FileManager,
    private val repository: VoiceRecordRepository
) {
    suspend fun deleteRecording(voiceRecord: VoiceRecord): Boolean {
        val filePath = voiceRecord.filePath
        val fileIdInDatabase = voiceRecord.id
        val success = fileManager.deleteFile(filePath)
        repository.delete(fileIdInDatabase)
        return success
    }
}