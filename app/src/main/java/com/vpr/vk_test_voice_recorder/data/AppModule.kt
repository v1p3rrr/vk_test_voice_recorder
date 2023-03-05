package com.vpr.vk_test_voice_recorder.data

import android.content.Context
import android.media.AudioRecord
import androidx.room.Room
import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordDao
import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordDatabase
import com.vpr.vk_test_voice_recorder.data.recorder.AudioFileSaverImpl
import com.vpr.vk_test_voice_recorder.data.recorder.AudioPlayerImpl
import com.vpr.vk_test_voice_recorder.data.recorder.AudioRecorderImpl
import com.vpr.vk_test_voice_recorder.data.repository.VoiceRecordRepositoryImpl
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import com.vpr.vk_test_voice_recorder.domain.recorder.AudioRecorder
import com.vpr.vk_test_voice_recorder.domain.saver.AudioFileSaver
import com.vpr.vk_test_voice_recorder.domain.usecase.AudioRecordingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideVoiceRecordDatabase(@ApplicationContext context: Context): VoiceRecordDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            VoiceRecordDatabase::class.java,
            "voicerecords_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideVoiceRecordDao(voiceRecordDatabase: VoiceRecordDatabase) = voiceRecordDatabase.voiceRecordDao()

    @Singleton
    @Provides
    fun provideVoiceRecordRepository(voiceRecordDao: VoiceRecordDao): VoiceRecordRepository = VoiceRecordRepositoryImpl(voiceRecordDao)

    @Singleton
    @Provides
    fun provideAudioRecorder(@ApplicationContext context: Context): AudioRecorder = AudioRecorderImpl(context)

    @Singleton
    @Provides
    fun provideAudioPlayer(@ApplicationContext context: Context): AudioPlayer = AudioPlayerImpl(context)

    @Singleton
    @Provides
    fun provideAudioFileSaver(@ApplicationContext context: Context): AudioFileSaver = AudioFileSaverImpl(context)

    @Singleton
    @Provides
    fun provideAudioRecordingUseCase(recorder: AudioRecorder, saver: AudioFileSaver, repository: VoiceRecordRepository): AudioRecordingUseCase = AudioRecordingUseCase(recorder, saver, repository)


}