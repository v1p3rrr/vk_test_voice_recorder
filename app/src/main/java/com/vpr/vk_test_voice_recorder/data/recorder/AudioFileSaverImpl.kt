package com.vpr.vk_test_voice_recorder.data.recorder

import android.content.Context
import com.vpr.vk_test_voice_recorder.domain.saver.AudioFileSaver
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioFileSaverImpl @Inject constructor(private val context: Context) : AudioFileSaver {

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

    private fun getAudioFilePath(name: String): String {
        val dir = context.getExternalFilesDir(null)
        return "${dir?.absolutePath}/$name"
    }
}