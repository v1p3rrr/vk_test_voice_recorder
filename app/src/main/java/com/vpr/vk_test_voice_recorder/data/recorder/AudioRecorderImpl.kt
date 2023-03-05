package com.vpr.vk_test_voice_recorder.data.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.vpr.vk_test_voice_recorder.domain.recorder.AudioRecorder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecorderImpl @Inject constructor(
    private val context: Context
) : AudioRecorder {

    private val DATETIME_FORMAT = "yyyyMMdd_HHmmss"

    private var recorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var startTime: Long = 0
    private var endTime: Long = 0

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    @Throws(IOException::class)
    private fun createAudioFile(): File {
        val timeStamp = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault()).format(Date())
        val audioFileName = "AUDIO_$timeStamp.mp3"
        val audioDir = context.filesDir
        return File(audioDir, audioFileName)
    }

    override fun start(): Boolean {
        try {
            audioFile = createAudioFile()
            createRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()

                startTime = System.currentTimeMillis()
                recorder = this

                val amplitude = recorder!!.maxAmplitude
                println(amplitude)
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    override fun stop() {
        recorder?.apply {
                val amplitude = recorder!!.maxAmplitude
                println(amplitude)
                // delay for 100 milliseconds
                Thread.sleep(100)
            stop()
            endTime = System.currentTimeMillis()
            reset()
            release()
            recorder = null
        }
    }

    override fun getRecordedFile(): File? {
        return audioFile
    }

    override fun getRecordDuration(): Long? {
        var resultTime: Long? = null
        if (startTime!=0L){
            resultTime = endTime - startTime
        }
        startTime = 0
        endTime = 0
        return resultTime
    }

}