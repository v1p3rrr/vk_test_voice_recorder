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
    private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : AudioPlayer {

    private var player: MediaPlayer? = null
    private var progressJob: Job? = null
    private var playbackPosition: Int = 0
    private val _currentPlayerPosition = MutableStateFlow<Int>(0)
    override val currentPlayerPosition: StateFlow<Int> = _currentPlayerPosition
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    override fun playFile(file: File, position: Int?) {
        try {
            val fileDescriptor = FileInputStream(file).fd
            player = MediaPlayer().apply {
                setDataSource(fileDescriptor)
                prepare()
                seekTo(position ?: playbackPosition)
                start()
                println("STARTED")
            }
            _isPlaying.value = true
            progressJob = updateProgress()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun pause() {
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
        } finally {
            progressJob?.cancel()
            progressJob = null
        }
    }

    override fun stop() {
        player?.apply {
            stop()
            reset()
            playbackPosition = 0
            _currentPlayerPosition.value = 0
            player = null
            _isPlaying.value = false
            progressJob?.cancel()
            progressJob = null
        }
    }

    private fun updateProgress() = CoroutineScope(defaultDispatcher).launch {
        player?.apply {
            while (true) {
                player?.let { player ->
                    if (_isPlaying.value) {
                        _currentPlayerPosition.value = player.currentPosition
                        println("currentPlayerPosition.value: " + _currentPlayerPosition.value)
                    }
                    if (player.currentPosition == player.duration) {
                        stop()
                        println("STOPPED")
                        _currentPlayerPosition.value = 0
                        progressJob?.cancel()
                        progressJob = null
                        return@launch
                    }
                } ?: run {
                    _currentPlayerPosition.value = 0
                    progressJob?.cancel()
                    progressJob = null
                    return@launch
                }
                delay(100L)
            }
        }
    }
}