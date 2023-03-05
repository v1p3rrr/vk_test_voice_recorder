package com.vpr.vk_test_voice_recorder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VoiceRecordEntity::class], version = 1)
abstract class VoiceRecordDatabase : RoomDatabase() {
    abstract fun voiceRecordDao(): VoiceRecordDao

    companion object {
        const val DB_NAME = "voicerecords_database"
    }
}