package com.vpr.vk_test_voice_recorder.domain.player

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AudioPlayer {
    val currentPlayerPosition: StateFlow<Int>
    fun playFile(file: File, position: Int?)
    fun stop()
    fun pause()
}