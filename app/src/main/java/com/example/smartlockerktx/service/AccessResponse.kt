package com.example.smartlockerktx.service

import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.database.model.Locker
import com.google.gson.annotations.SerializedName

class AccessResponse {
    @SerializedName("data")
    var access: ArrayList<Access> = ArrayList()
}