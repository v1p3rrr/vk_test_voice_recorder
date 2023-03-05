package com.vpr.vk_test_voice_recorder.domain.saver

import java.io.File

interface FileManager {
    fun saveFile(file: File): Boolean
    fun deleteFile(filePath: String): Boolean
}