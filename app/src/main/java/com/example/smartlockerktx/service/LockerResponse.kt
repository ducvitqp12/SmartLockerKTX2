package com.example.smartlockerktx.service

import com.example.smartlockerktx.database.model.GroupLockerEmployee
import com.example.smartlockerktx.database.model.Locker
import com.google.gson.annotations.SerializedName

class LockerResponse {
    @SerializedName("data")
    var locker: ArrayList<Locker> = ArrayList()
}