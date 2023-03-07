package com.vpr.vk_test_voice_recorder.domain.usecase.audio

import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.player.FileManager
import com.vpr.vk_test_voice_recorder.domain.repository.VoiceRecordRepository

class AudioRenameUseCase(
    private val fileManager: FileManager,
    private val repository: VoiceRecordRepository
) {

    suspend fun rename(voiceRecord: VoiceRecord, newName: String): Boolean {
        if (newName.isNotBlank()) {
            val newPath = fileManager.renameFile(filePath = voiceRecord.filePath, newName = newName)
            if (newPath != null) {
                repository.updateNameAndFilePath(
                    id = voiceRecord.id,
                    newName = newName,
                    filePath = newPath
                )
                return true
            }
        }
        return false
    }
}