package com.vpr.vk_test_voice_recorder.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    fun provideDateTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatter()
    }

    @Provides
    fun provideTimeDifferenceCalculator(): TimeDifferenceCalculator {
        return TimeDifferenceCalculator()
    }

    @Provides
    fun provideDateFormatter(): DateFormatter {
        return DateFormatter()
    }

}