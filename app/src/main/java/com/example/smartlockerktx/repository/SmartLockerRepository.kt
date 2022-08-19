package com.example.smartlockerktx.repository

import android.app.Application
import android.util.Log
import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.database.SmartLockerDatabase
import com.example.smartlockerktx.database.model.Employee
import com.example.smartlockerktx.database.model.GroupLockerEmployee
import com.example.smartlockerktx.database.model.Locker


class SmartLockerRepository() {

    lateinit var db: SmartLockerDatabase

    constructor(application: Application) : this() {
        db = SmartLockerDatabase.getDatabase(application)
    }

    suspend fun updateLockerStatus(lockerId: String, status: Int){
        val locker = db.lockerDAO().getLocker(lockerId)
        locker.available = status
        updateLocker(locker)
    }

    suspend fun updateAccess(access: Access): Long{
        Log.d("Abcd", "Access " + access.accessId )
        return db.accessDAO().insert(access)
    }

    suspend fun updateAccess(lockerId: String, accessTime: String, status: String, employeeId: String){
        if(employeeId == "") db.accessDAO().updateByLocker(lockerId, accessTime, status)
        else db.accessDAO().updateByLocker(lockerId, accessTime, status, employeeId)
    }

    suspend fun getLocker(page: Int): List<Locker>{
        return db.lockerDAO().getLocker(page)
    }

    suspend fun getEmployeeLockers(groupEmployeeId: String): List<Locker>{
        val gle = db.groupLockerEmployeeDAO().getGleByGe(groupEmployeeId).groupLockerId
        return db.lockerDAO().getLockerByGroupLockerId(gle)
    }

    suspend fun getEmployee(): List<Employee>{
        return db.employeeDAO().getAllEmployees()
    }

    suspend fun updateLocker(locker: Locker): Long{
        return db.lockerDAO().insert(locker)
    }
    suspend fun updateEmployee(employee: Employee): Long{
        return db.employeeDAO().insert(employee)
    }
    suspend fun updateGroupLockerEmployee(groupLockerEmployee: GroupLockerEmployee): Long{
        return db.groupLockerEmployeeDAO().insert(groupLockerEmployee)
    }

    suspend fun getAccess(id :  String): Access{
        return db.accessDAO().getAccessByLockerId(id)
    }

    suspend fun deleteAllTable(){
        db.employeeDAO().delete()
        db.accessDAO().deleteAccess()
        db.lockerDAO().deleteLocker()
        db.groupLockerEmployeeDAO().deleteGle()
    }

}