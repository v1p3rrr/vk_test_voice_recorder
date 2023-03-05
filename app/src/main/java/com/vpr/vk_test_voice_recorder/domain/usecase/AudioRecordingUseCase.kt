package com.vpr.vk_test_voice_recorder.domain.usecase

import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordEntity
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.recorder.AudioRecorder
import com.vpr.vk_test_voice_recorder.domain.saver.AudioFileSaver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecordingUseCase @Inject constructor(
    private val recorder: AudioRecorder,
    private val saver: AudioFileSaver,
    private val repository: VoiceRecordRepository
) {
    suspend fun startRecording(): Boolean {
        return recorder.start()
    }

    suspend fun stopRecordingAndSave(): Boolean {
        recorder.stop()
        val audioFile = recorder.getRecordedFile()
        val duration = recorder.getRecordDuration()
        return if (audioFile != null && saver.saveFile(audioFile) && duration != null) {
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