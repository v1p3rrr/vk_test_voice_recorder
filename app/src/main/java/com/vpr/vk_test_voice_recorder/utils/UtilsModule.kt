package com.vpr.vk_test_voice_recorder.utils

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideDateTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter = DateTimeFormatter(context)

    @Provides
    @Singleton
    fun provideTimeDifferenceCalculator(): TimeDifferenceCalculator = TimeDifferenceCalculator()
}