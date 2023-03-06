package com.vpr.vk_test_voice_recorder.domain.model

data class PlayerState(
    var isPlaying: Boolean,
    var currentPlayerPosition: Int,
    var voiceRecord: VoiceRecord?
)
