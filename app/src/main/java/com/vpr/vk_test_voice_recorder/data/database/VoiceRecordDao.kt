package com.vpr.vk_test_voice_recorder.data.database

import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface VoiceRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoiceRecord(voiceRecord: VoiceRecordEntity)

    @Query("SELECT * FROM voice_records ORDER BY timestamp DESC")
    fun getAllVoiceRecords(): Flow<List<VoiceRecordEntity>>

    @Query("SELECT * FROM voice_records WHERE id = :id")
    fun getVoiceRecordById(id: Long): VoiceRecordEntity?

    @Query("DELETE FROM voice_records WHERE id = :id")
    fun deleteVoiceRecordById(id: Long)

    @Query("UPDATE voice_records SET name = :newName WHERE id = :id")
    fun updateVoiceRecordNameById(id: Long, newName: String)
}