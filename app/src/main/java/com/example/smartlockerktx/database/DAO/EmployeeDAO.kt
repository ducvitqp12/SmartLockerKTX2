package com.example.smartlockerktx.database.DAO

import androidx.room.*
import com.example.smartlockerktx.database.model.Employee


@Dao
interface EmployeeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee): Long
    @Update
    suspend fun update(employee: Employee): Int
    @Query("SELECT * from Employee ")
    suspend fun getAllEmployees(): List<Employee>
    @Query("SELECT * from Employee WHERE employeeId = (:id)" )
    suspend fun getEmployee(id: String): Employee
    @Query("SELECT * from Employee WHERE codeLogic = (:codeLogic)" )
    suspend fun getEmployeeByRFID(codeLogic: String): Employee?
    @Query("SELECT * from Employee WHERE pinCode = (:pinCode)" )
    fun getEmployeeByPinCode(pinCode: String): Employee?
    @Query("SELECT * from Employee WHERE username = (:username)" )
    fun getEmployeeByUsername(username: String): Employee?
    @Query("DELETE FROM Employee")
    suspend fun delete()
}