package com.vpr.vk_test_voice_recorder.domain.usecase

import com.vpr.vk_test_voice_recorder.data.di.DefaultDispatcher
import com.vpr.vk_test_voice_recorder.domain.model.PlayerState
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PlayAudioUseCase @Inject constructor(
    private val player: AudioPlayer,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    private var currentRecord: VoiceRecord? = null
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState(false, 0, null))
    val playerState: StateFlow<PlayerState> = _playerState

    init {
        CoroutineScope(defaultDispatcher).launch {
            println("Collecting isPlaying flow")
            launch {
                player.isPlaying.collect {
                    _playerState.value = _playerState.value.copy(isPlaying = it)
                }
            }
            println("Collecting currentPlayerPosition flow")
            launch {
                player.currentPlayerPosition.collect {
                    _playerState.value = _playerState.value.copy(currentPlayerPosition = it)
                }
            }
        }
    }

    suspend fun playAudio(voiceRecord: VoiceRecord, position: Int): Boolean {
        delay(50L)
        var actualPosition: Int? = position
        if (currentRecord?.id!=voiceRecord.id){
            stopAudio()
            actualPosition = null
        }
        currentRecord = voiceRecord
        _playerState.value.voiceRecord = currentRecord
        try {
            val filePath = voiceRecord.filePath
            val file = File(filePath)
            if (file.exists()) {
                println(filePath)
                println(file.exists())
                player.playFile(file, actualPosition)
                return true
            }
            return false
        } catch (e: IOException) {
            stopAudio()
            e.printStackTrace()
            return false
        }
    }

    fun getCurrentRecord(): VoiceRecord? {
        return currentRecord
    }

    suspend fun pauseAudio(): Boolean {
        delay(50L)
        return try {
            player.pausePlayer()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun stopAudio(): Boolean {
        delay(50L)
        return try {
            player.stopPlayer()
            currentRecord = null
            _playerState.value.voiceRecord = currentRecord
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}