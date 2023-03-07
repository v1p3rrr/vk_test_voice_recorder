package com.vpr.vk_test_voice_recorder.domain.usecase.audio

import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordEntity
import com.vpr.vk_test_voice_recorder.domain.repository.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.player.AudioRecorder
import com.vpr.vk_test_voice_recorder.domain.player.FileManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecordingUseCase @Inject constructor(
    private val recorder: AudioRecorder,
    private val fileManager: FileManager,
    private val repository: VoiceRecordRepository
) {
    suspend fun startRecording(): Boolean {
        return recorder.start()
    }

    suspend fun stopRecordingAndSave(): Boolean {
        recorder.stop()
        val audioFile = recorder.getRecordedFile()
        val duration = recorder.getRecordDuration()
        return if (audioFile != null && fileManager.saveFile(audioFile) && duration != null) {
            val voiceRecord = VoiceRecordEntity(
                name = audioFile.name,
                filePath = audioFile.absolutePath,
                duration = duration,
                timestamp = System.currentTimeMillis()
            )
            repository.insert(voiceRecord)
            true
        } else false
    }

}