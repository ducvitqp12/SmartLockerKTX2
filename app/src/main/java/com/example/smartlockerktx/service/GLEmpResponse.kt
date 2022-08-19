package com.example.smartlockerktx.service

import com.example.smartlockerktx.database.model.GroupLockerEmployee
import com.google.gson.annotations.SerializedName

class GLEmpResponse {
    @SerializedName("gles")
    var gles: ArrayList<GroupLockerEmployee> = ArrayList()
}

