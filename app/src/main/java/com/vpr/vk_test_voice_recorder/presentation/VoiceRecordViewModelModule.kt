package com.vpr.vk_test_voice_recorder.presentation

import com.vpr.vk_test_voice_recorder.data.di.DefaultDispatcher
import com.vpr.vk_test_voice_recorder.data.di.IoDispatcher
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import com.vpr.vk_test_voice_recorder.domain.recorder.AudioRecorder
import com.vpr.vk_test_voice_recorder.domain.saver.FileManager
import com.vpr.vk_test_voice_recorder.domain.usecase.AudioDeletionUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.AudioRecordingUseCase
import com.vpr.vk_test_voice_recorder.domain.usecase.PlayAudioUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object VoiceRecordViewModelModule {

    @Provides
    fun provideAudioRecordingUseCase(recorder: AudioRecorder, fileManager: FileManager, repository: VoiceRecordRepository): AudioRecordingUseCase = AudioRecordingUseCase(recorder, fileManager, repository)

    @Provides
    fun provideAudioDeletionUseCase(fileManager: FileManager, repository: VoiceRecordRepository): AudioDeletionUseCase = AudioDeletionUseCase(fileManager, repository)

    @Provides
    fun providePlayAudioUseCase(player: AudioPlayer, @IoDispatcher dispatcher: CoroutineDispatcher): PlayAudioUseCase = PlayAudioUseCase(player, dispatcher)

}