package com.example.smartlockerktx.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History (
    var accessTime: String? = null,
    var action: String? = null,
    var doorState: String? = null,
    var employeeId : String,
    var health: String? = null,
    @PrimaryKey
    @ColumnInfo(name = "historyId")
    var historyId: String,
    var lockerId: String,
    var recvData: String? = null,
    var rfidTag: String? = null,
    var status: String? = null
)

{
    override fun toString(): String {
        return """{ "accessTime" : "${this.accessTime}", "action": "${this.action}","lockerId" : "${this.lockerId}", "doorState": "${this.doorState}" , "employeeId": "${this.employeeId}","status": "${this.status}", "health": ${this.health}}"""
    }
}