package com.example.smartlockerktx.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Employee (
    @ColumnInfo(name = "codeLogic")
    var codeLogic: String? = null,
    @ColumnInfo(name = "departmentID")
    var departmentID: String,
    @ColumnInfo(name = "employeeCode")
    var employeeCode: String? = null,
    @PrimaryKey
    @ColumnInfo(name = "employeeId")
    var employeeId : String,
    @ColumnInfo(name = "familyName")
    var familyName: String? = null,
    @ColumnInfo(name = "groupEmployeeId")
    var groupEmployeeId :String,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "pinCode")
    var pinCode: String? = null,
    @ColumnInfo(name = "username")
    var username: String? = null,
    @ColumnInfo(name = "password")
    var password: String? = null
)