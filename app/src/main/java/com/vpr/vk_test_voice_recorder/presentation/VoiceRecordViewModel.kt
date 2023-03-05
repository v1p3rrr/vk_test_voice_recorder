package com.vpr.vk_test_voice_recorder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordEntity
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.model.VoiceRecord
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import com.vpr.vk_test_voice_recorder.domain.usecase.AudioRecordingUseCase
import com.vpr.vk_test_voice_recorder.utils.DateFormatter
import com.vpr.vk_test_voice_recorder.utils.DateTimeFormatter
import com.vpr.vk_test_voice_recorder.utils.TimeDifferenceCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VoiceRecordViewModel @Inject constructor(
    private val repository: VoiceRecordRepository,
    private val audioRecordingUseCase: AudioRecordingUseCase,
    private val player: AudioPlayer,
    private val dateTimeFormatter: DateTimeFormatter
) :
    ViewModel() {

    private val _voiceRecords = MutableStateFlow<List<VoiceRecord>>(emptyList())
    public val voiceRecords: StateFlow<List<VoiceRecord>> = _voiceRecords

    init {
        viewModelScope.launch {
            repository.allVoiceRecords
                .map { voiceRecordList ->
                    voiceRecordList.map { voiceRecordEntity ->
                        VoiceRecord(
                            id = voiceRecordEntity.id,
                            name = voiceRecordEntity.name,
                            duration = dateTimeFormatter.getTimeWithDaysAsHours(voiceRecordEntity.duration),
                            filePath = voiceRecordEntity.filePath,
                            date = dateTimeFormatter.getDate(voiceRecordEntity.timestamp),
                            time = dateTimeFormatter.getTime(voiceRecordEntity.timestamp)
                        )
                    }
                }
                .collect { mappedList ->
                    _voiceRecords.value = mappedList
                }
        }
    }

    fun insertVoiceRecord(voiceRecord: VoiceRecordEntity) = viewModelScope.launch {
        repository.insert(voiceRecord)
    }

    suspend fun startRecorder() {
        audioRecordingUseCase.startRecording()
    }

    suspend fun stopRecorderAndSave() {
        audioRecordingUseCase.stopRecordingAndSave()
    }

    fun startPlayer(file: File) {
        player.playFile(file)
    }

    fun stopPlayer() {
        player.stop()
    }
}

