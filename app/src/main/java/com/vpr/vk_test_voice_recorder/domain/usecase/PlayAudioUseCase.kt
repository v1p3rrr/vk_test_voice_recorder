package com.vpr.vk_test_voice_recorder.domain.usecase

import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PlayAudioUseCase @Inject constructor(
    private val player: AudioPlayer
) {
    private var currentRecord: VoiceRecord? = null

    suspend fun playAudio(voiceRecord: VoiceRecord, position: Int): Boolean {
        var actualPosition: Int? = position
        if (currentRecord!=voiceRecord){
            stopAudio()
            actualPosition = null
        }
        currentRecord = voiceRecord
        try {
            val filePath = voiceRecord.filePath
            val file = File(filePath)
            if (file.exists()) {
                println(filePath)
                println(file)
                player.playFile(file, actualPosition)
                return true
            }
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    fun getCurrentRecord(): VoiceRecord? {
        return currentRecord
    }

    suspend fun pauseAudio(): Boolean {
        return try {
            player.pause()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun stopAudio(): Boolean {
        return try {
            player.stop()
            currentRecord = null
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}