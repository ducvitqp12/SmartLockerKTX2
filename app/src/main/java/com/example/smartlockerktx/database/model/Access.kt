package com.example.smartlocker.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Access (
    @PrimaryKey
    @ColumnInfo(name = "accessId")
    @SerializedName("_id")
    var accessId: String,
    @SerializedName("accessTime")
    @ColumnInfo(name = "accessTime")
    var accessTime: String? = null,
    @ColumnInfo(name = "action")
    @SerializedName("action")
    var action: String? = null,
    @SerializedName("dirty")
    @ColumnInfo(name = "dirty")
    var dirty: Int = 0,
    @SerializedName("doorState")
    @ColumnInfo(name = "doorState")
    var doorState: String? = null,
    @SerializedName("employeeId")
    @ColumnInfo(name = "employeeId")
    var employeeId: String? = null,
    @SerializedName("health")
    @ColumnInfo(name = "health")
    var health: String? = null,
    @SerializedName("lockerId")
    @ColumnInfo(name = "lockerId")
    var lockerId: String? = null,
    @SerializedName("recvData")
    @ColumnInfo(name = "recvData")
    var recvData: String? = null,
    @SerializedName("status")
    @ColumnInfo(name = "status")
    var status: String? = null
){
    override fun toString(): String {
        return """{"accessId": "${this.accessId}", "accessTime" : "${this.accessTime}", "action": ${ if (this.action.isNullOrBlank()) null else "\"" + this.action + "\"" }, "lockerId" : ${ if (this.lockerId.isNullOrBlank()) null else "\"" + this.lockerId + "\"" }, "doorState": ${ if (this.doorState.isNullOrBlank()) null else "\"" + this.doorState + "\"" } , "employeeId": ${ if (this.employeeId.isNullOrBlank()) null else "\"" + this.employeeId + "\"" },"status": ${ if (this.status.isNullOrBlank()) null else "\"" + this.status + "\"" }, "health": ${ if (this.health.isNullOrBlank()) null else "\"" + this.health + "\"" }}"""
    }
}