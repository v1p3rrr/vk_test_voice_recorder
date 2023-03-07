package com.vpr.vk_test_voice_recorder.data.recorder

import android.content.Context
import com.vpr.vk_test_voice_recorder.domain.player.FileManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManagerImpl @Inject constructor(private val context: Context) : FileManager {

    override fun saveFile(file: File): Boolean {
        try {
            val output = FileOutputStream(getAudioFilePath(file.name))
            val input = file.inputStream()
            input.copyTo(output)
            input.close()
            output.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun deleteFile(filePath: String): Boolean {
        try {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun renameFile(filePath: String, newName: String) : String? {
        try {
            val oldFile = File(filePath)
            val oldFileDirectory = oldFile.parent
            if (oldFile.exists() && oldFileDirectory != null) {
                val newFileDirectory = "$oldFileDirectory/$newName.mp3"
                val newFile = File(newFileDirectory)
                if (!newFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        return newFileDirectory
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getAudioFilePath(name: String): String {
        val dir = context.getExternalFilesDir(null)
        return "${dir?.absolutePath}/$name"
    }


}