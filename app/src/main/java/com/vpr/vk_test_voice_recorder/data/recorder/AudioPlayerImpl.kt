package com.vpr.vk_test_voice_recorder.data.recorder

import android.content.Context
import android.media.MediaPlayer
import com.vpr.vk_test_voice_recorder.data.di.DefaultDispatcher
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileInputStream

class AudioPlayerImpl(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : AudioPlayer {

    private var player: MediaPlayer? = null
    private var progressJob: Job? = null
    private var playbackPosition: Int = 0
    private val _currentPlayerPosition = MutableStateFlow<Int>(0)
    override val currentPlayerPosition: StateFlow<Int> = _currentPlayerPosition
    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying

    override fun playFile(file: File, position: Int?) {
        try {
            println(file)
            val fileDescriptor = FileInputStream(file).fd
            player = MediaPlayer().apply {
                setDataSource(fileDescriptor)
                prepare()
                seekTo(position ?: playbackPosition)
                start()
                _currentPlayerPosition.value = position ?: playbackPosition
                println("STARTED")
            }
            _isPlaying.value = true
            progressJob = updateProgress()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun pausePlayer() {
        try {
            player?.apply {
                if (this.isPlaying) {
                    playbackPosition = currentPosition
                    pause()
                    println("PAUSED")
                    _isPlaying.value = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            player = null
        } finally {
            progressJob?.cancel()
            progressJob = null
        }
    }

    override fun stopPlayer() {
        player?.apply {
            stop()
            reset()
        }
        playbackPosition = 0
        _currentPlayerPosition.value = 0
        player = null
        _isPlaying.value = false
        progressJob?.cancel()
        progressJob = null
        println("STOPPED-")
    }

    private fun updateProgress() = CoroutineScope(defaultDispatcher).launch {
        player?.apply {
            while (isActive && currentPosition < duration) {
                if (_isPlaying.value) {
                    _currentPlayerPosition.value = currentPosition
                    println(currentPosition)
                }
                delay(20L)
                if (!isPlaying || currentPosition >= duration) {
                    _currentPlayerPosition.value = duration
                    delay(20L)
                    stopPlayer()
                    println("AUDIO ENDED, STOPPED")
                    progressJob?.cancel()
                    progressJob = null
                }
            }
        }
    }
}