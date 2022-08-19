package com.example.smartlockerktx.database.DAO

import androidx.room.*
import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.database.model.History

@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History): Long
    @Query("SELECT * from History ")
    suspend fun getAllHistory(): List<History>
}