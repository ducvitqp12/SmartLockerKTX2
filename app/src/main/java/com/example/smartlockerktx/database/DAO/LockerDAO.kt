package com.example.smartlockerktx.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smartlockerktx.database.model.Employee
import com.example.smartlockerktx.database.model.Locker

@Dao
interface LockerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locker: Locker): Long
    @Update
    suspend fun update(locker: Locker): Int
    @Query("SELECT * from Locker ")
    suspend fun getAllLocker(): List<Locker>
    @Query("SELECT * from Locker WHERE page = (:page)")
    suspend fun getLocker(page: Int): List<Locker>
    @Query("SELECT * from Locker WHERE lockerId = (:id)" )
    suspend fun getLocker(id: String): Locker
    @Query("SELECT * from Locker WHERE groupLockerId = (:glid)" )
    suspend fun getLockerByGroupLockerId(glid: String): List<Locker>
    @Query("DELETE FROM Locker")
    fun deleteLocker()
}