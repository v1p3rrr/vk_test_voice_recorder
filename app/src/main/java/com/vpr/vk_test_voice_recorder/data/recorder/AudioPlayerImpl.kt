package com.vpr.vk_test_voice_recorder.data.recorder

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import java.io.File

class AudioPlayerImpl(
    private val context: Context
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        player = MediaPlayer.create(context, file.toUri())
        player?.start()
    }

    override fun stop() {
        player?.apply {
            stop()
            release()
            player = null
        }
    }
}