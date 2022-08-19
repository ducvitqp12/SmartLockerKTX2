package com.example.smartlockerktx.database.DAO

import androidx.room.*
import com.example.smartlockerktx.database.model.GroupLockerEmployee

@Dao
interface GroupLockerEmployeeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupLockerEmployee: GroupLockerEmployee): Long
    @Update
    suspend fun update(groupLockerEmployee: GroupLockerEmployee): Int
    @Query("SELECT * from GroupLockerEmployee")
    suspend fun getAllGroupLockerEmployee(): List<GroupLockerEmployee>
    @Query("SELECT * from GroupLockerEmployee WHERE groupLockerEmployeeId = (:groupLockerEmployeeId)")
    suspend fun getGroupLockerEmployee(groupLockerEmployeeId: String): GroupLockerEmployee
    @Query("SELECT * from GroupLockerEmployee WHERE groupEmployeeId = (:groupEmployeeId)")
    suspend fun getGleByGe(groupEmployeeId: String): GroupLockerEmployee
    @Query("DELETE FROM GroupLockerEmployee")
    fun deleteGle()
}