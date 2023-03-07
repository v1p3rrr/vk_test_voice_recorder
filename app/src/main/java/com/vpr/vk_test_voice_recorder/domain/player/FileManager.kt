package com.vpr.vk_test_voice_recorder.domain.player

import java.io.File

interface FileManager {
    fun saveFile(file: File): Boolean
    fun deleteFile(filePath: String): Boolean
    fun renameFile(filePath: String, newName: String): String?
}