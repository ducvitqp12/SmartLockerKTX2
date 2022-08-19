package com.example.smartlockerktx.database.DAO

import androidx.room.*
import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.database.model.AdminTag

@Dao
interface AdminTagDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(adminTag: AdminTag): Long
    @Update
    suspend fun update(adminTag: AdminTag): Int
    @Query("SELECT * from AdminTag WHERE RFIDID = (:id)" )
    suspend fun getAdminTag(id: String): AdminTag
    @Query("SELECT * from AdminTag WHERE RFIDCode = (:id)" )
    suspend fun getAdminTagByRFID(id: String): AdminTag
}