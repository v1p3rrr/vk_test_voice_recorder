package com.vpr.vk_test_voice_recorder.domain.player

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AudioPlayer {
    val currentPlayerPosition: StateFlow<Int>
    val isPlaying: StateFlow<Boolean>
    fun playFile(file: File, position: Int?)
    fun stopPlayer()
    fun pausePlayer()
}