package com.vpr.vk_test_voice_recorder.domain.player

import java.io.File

interface AudioRecorder {
    fun start(): Boolean
    fun stop()
    fun getRecordedFile(): File?
    fun getRecordDuration(): Long?
}