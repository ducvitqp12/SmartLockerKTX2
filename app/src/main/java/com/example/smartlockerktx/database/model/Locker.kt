package com.example.smartlockerktx.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Locker constructor(
    @PrimaryKey
    @ColumnInfo(name = "lockerId")
    @SerializedName("_id")
    var lockerId: String,
    @SerializedName("available")
    var available: Int = 0,
    @SerializedName("column")
    var column: Int = 0,
    @SerializedName("controllerDeviceImei")
    var controllerDeviceImei: String? = null,
    @ColumnInfo(name = "groupLockerId")
    @SerializedName("groupLockerId")
    var groupLockerId : String,
    @SerializedName("label")
    var label: String? = null,
    @SerializedName("number")
    var number: Int = 0,
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("position")
    var position: String? = null,
    @SerializedName("row")
    var row: Int = 0,
    var doorState: String? = "CLOSE"
)