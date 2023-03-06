package com.vpr.vk_test_voice_recorder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpr.vk_test_voice_recorder.data.di.IoDispatcher
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.model.PlayerState
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import com.vpr.vk_test_voice_recorder.domain.usecase.AudioDeletionUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.AudioRecordingUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.PlayAudioUseCase
import com.vpr.vk_test_voice_recorder.utils.DateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VoiceRecordViewModel @Inject constructor(
    private val repository: VoiceRecordRepository,
    private val audioRecordingUseCase: AudioRecordingUseCase,
    private val audioDeletionUseCase: AudioDeletionUseCase,
    private val playAudioUseCase: PlayAudioUseCase,
    val dateTimeFormatter: DateTimeFormatter,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    ViewModel() {

    val playerState = playAudioUseCase.playerState

    private val _voiceRecords = MutableStateFlow<List<VoiceRecord>>(emptyList())
    val voiceRecords: StateFlow<List<VoiceRecord>> = _voiceRecords

    init {
        viewModelScope.launch(ioDispatcher) {
            launch {
                repository.allVoiceRecords
                    .map { voiceRecordList ->
                        voiceRecordList.map { voiceRecordEntity ->
                            VoiceRecord(
                                id = voiceRecordEntity.id,
                                name = voiceRecordEntity.name,
                                duration = dateTimeFormatter.getDuration(voiceRecordEntity.duration),
                                filePath = voiceRecordEntity.filePath,
                                date = dateTimeFormatter.getDate(voiceRecordEntity.timestamp),
                                time = dateTimeFormatter.getHoursMinutesTime(voiceRecordEntity.timestamp),
                                timestamp = voiceRecordEntity.timestamp
                            )
                        }
                    }
                    .collect { mappedList ->
                        _voiceRecords.value = mappedList
                    }
            }
        }
    }

    suspend fun startRecorder() {
        audioRecordingUseCase.startRecording()
    }

    suspend fun stopRecorderAndSave() {
        audioRecordingUseCase.stopRecordingAndSave()
    }

    fun deleteRecord(voiceRecord: VoiceRecord) {
        viewModelScope.launch(ioDispatcher) {
            if (playAudioUseCase.getCurrentRecord()?.id == voiceRecord.id){
                playAudioUseCase.stopAudio()
            }
            audioDeletionUseCase.deleteRecording(voiceRecord)
        }
    }

    fun startPlayer(voiceRecord: VoiceRecord, position: Int) {
        viewModelScope.launch(ioDispatcher) {
            playAudioUseCase.playAudio(voiceRecord, position)
        }
    }

    fun pausePlayer() {
        viewModelScope.launch(ioDispatcher) {
            playAudioUseCase.pauseAudio()
            //player.pause()
        }
    }

    fun stopPlayer() {
        viewModelScope.launch(ioDispatcher) {
            playAudioUseCase.stopAudio()
            //player.stop()
        }
    }
}

