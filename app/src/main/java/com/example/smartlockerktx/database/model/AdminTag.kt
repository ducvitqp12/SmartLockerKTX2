package com.example.smartlockerktx.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AdminTag (
    @ColumnInfo(name = "RFIDCode")
    var RFIDCode: String? = null,
    @PrimaryKey
    @ColumnInfo(name = "RFIDID")
    var RFIDID: String,
    @ColumnInfo(name = "status")
    var status: String? = null

)