package com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpr.vk_test_voice_recorder.data.di.IoDispatcher
import com.vpr.vk_test_voice_recorder.data.di.MainDispatcher
import com.vpr.vk_test_voice_recorder.domain.repository.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.usecase.audio.AudioDeletionUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.audio.AudioRecordingUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.audio.AudioRenameUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.audio.PlayAudioUseCase
import com.vpr.vk_test_voice_recorder.presentation.ActionStatus
import com.vpr.vk_test_voice_recorder.utils.DateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VoiceRecordViewModel @Inject constructor(
    private val repository: VoiceRecordRepository,
    private val audioRecordingUseCase: AudioRecordingUseCase,
    private val audioDeletionUseCase: AudioDeletionUseCase,
    private val audioRenameUseCase: AudioRenameUseCase,
    private val playAudioUseCase: PlayAudioUseCase,
    val dateTimeFormatter: DateTimeFormatter,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) :
    ViewModel() {

    val playerState = playAudioUseCase.playerState

    private val _voiceRecords = MutableStateFlow<List<VoiceRecord>>(emptyList())
    val voiceRecords: StateFlow<List<VoiceRecord>> = _voiceRecords

    private val _statusMessageSharedFlow = MutableSharedFlow<ActionStatus>()
    val statusMessageSharedFlow = _statusMessageSharedFlow.asSharedFlow()

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
            val isSuccessful = audioDeletionUseCase.deleteRecording(voiceRecord)
            _statusMessageSharedFlow.emit(if (isSuccessful) ActionStatus.DELETE_SUCCESS else ActionStatus.DELETE_FAILED)
        }
    }

    fun renameRecord(voiceRecord: VoiceRecord?, newName: String){
        viewModelScope.launch(ioDispatcher) {
            var isSuccessful = false
            if (voiceRecord != null){
                isSuccessful = audioRenameUseCase.rename(voiceRecord, newName)
            }
            _statusMessageSharedFlow.emit(
                if (isSuccessful) ActionStatus.RENAME_SUCCESS else
                    ActionStatus.RENAME_FAILED)
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
        }
    }

    fun stopPlayer() {
        viewModelScope.launch(ioDispatcher) {
            playAudioUseCase.stopAudio()
        }
    }
}

