package com.vpr.vk_test_voice_recorder.domain.saver

import java.io.File

interface AudioFileSaver {
    fun saveFile(file: File): Boolean
}