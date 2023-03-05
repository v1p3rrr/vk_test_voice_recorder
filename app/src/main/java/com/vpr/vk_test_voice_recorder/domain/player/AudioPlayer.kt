package com.vpr.vk_test_voice_recorder.domain.player

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}