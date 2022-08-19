package com.example.smartlockerktx.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Command (
    @PrimaryKey
    @ColumnInfo(name = "commandId")
    var commandId: String,
    @ColumnInfo(name = "commandType")
    var commandType: Int = 0,
    @ColumnInfo(name = "lockerId")
    var lockerId: String,
    @ColumnInfo(name = "lockerNumber")
    var lockerNumber: Int = 0
)