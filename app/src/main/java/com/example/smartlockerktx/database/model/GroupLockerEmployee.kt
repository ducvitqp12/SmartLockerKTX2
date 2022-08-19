package com.example.smartlockerktx.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GroupLockerEmployee (
    @ColumnInfo(name = "groupEmployeeId")
    @SerializedName("groupEmployeeId")
    var groupEmployeeId : String,
    @PrimaryKey
    @SerializedName("_id")
    @ColumnInfo(name = "groupLockerEmployeeId")
    var groupLockerEmployeeId : String,
    @ColumnInfo(name = "groupLockerId")
    @SerializedName("groupLockerId")
    var groupLockerId : String,
)