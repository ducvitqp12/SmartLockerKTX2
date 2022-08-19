package com.example.smartlockerktx.database.DAO

import androidx.room.*
import com.example.smartlocker.database.model.Access

@Dao
interface AccessDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(access: Access): Long
    @Update
    suspend fun update(access: Access): Int
    @Query("UPDATE Access SET `accessTime` = (:accessTime), `status` = (:status)," +
            " `employeeId` = (:employeeId)  WHERE lockerId = (:lockerId)" )
    suspend fun updateByLocker(lockerId: String, accessTime: String, status: String, employeeId: String)
    @Query("UPDATE Access SET `accessTime` = (:accessTime), `status` = (:status)," +
            " `employeeId` = null  WHERE lockerId = (:lockerId)" )
    suspend fun updateByLocker(lockerId: String, accessTime: String, status: String)
    @Query("SELECT * from Access ")
    suspend fun getAllAccess(): List<Access>
    @Query("SELECT * from Access WHERE dirty = 1")
    suspend fun getDirtyAccess(): List<Access>
    @Query("SELECT * from Access WHERE lockerId = (:id)" )
    suspend fun getAccessByLockerId(id: String): Access

    @Query("DELETE FROM Access")
    fun deleteAccess()
}