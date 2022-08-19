package com.example.smartlockerktx.service

import com.example.smartlockerktx.database.model.Employee
import com.google.gson.annotations.SerializedName

class EmployeeResponse {
    @SerializedName("employees")
    var employees: ArrayList<SubEmployee> = ArrayList()
    @SerializedName("accounts")
    var accounts: ArrayList<Account> = ArrayList()
}

class SubEmployee {
    @SerializedName("_id")
    var _id : String? = ""

    @SerializedName("createdBy")
    var createdBy: String? = null

    @SerializedName("groupEmployeeId")
    var groupEmployeeId: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("departmentID")
    var departmentID: String? = null

    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("pinCode")
    var pinCode: String? = null

    @SerializedName("__v")
    var __v: Int = 0
    @SerializedName("employeeCode")
    var employeeCode: String? = null

    @SerializedName("name")
    var name: String? = null

}

class Account{
    @SerializedName("_id")
    var _id : String? = null

    @SerializedName("createdBy")
    var createdBy: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("userName")
    var userName: String? = null

    @SerializedName("createdAt")
    var createdAt: String? = null
}