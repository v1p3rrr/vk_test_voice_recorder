package com.vpr.vk_test_voice_recorder.data.di

import android.content.Context
import androidx.room.Room
import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordDao
import com.vpr.vk_test_voice_recorder.data.database.VoiceRecordDatabase
import com.vpr.vk_test_voice_recorder.data.recorder.FileManagerImpl
import com.vpr.vk_test_voice_recorder.data.recorder.AudioPlayerImpl
import com.vpr.vk_test_voice_recorder.data.recorder.AudioRecorderImpl
import com.vpr.vk_test_voice_recorder.data.repository.VoiceRecordRepositoryImpl
import com.vpr.vk_test_voice_recorder.domain.VoiceRecordRepository
import com.vpr.vk_test_voice_recorder.domain.player.AudioPlayer
import com.vpr.vk_test_voice_recorder.domain.recorder.AudioRecorder
import com.vpr.vk_test_voice_recorder.domain.saver.FileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
    fun provideAudioPlayer(@ApplicationContext context: Context, @DefaultDispatcher dispatcher: CoroutineDispatcher): AudioPlayer = AudioPlayerImpl(context, dispatcher)

    @Singleton
    @Provides
    fun provideFileManager(@ApplicationContext context: Context): FileManager = FileManagerImpl(context)




}