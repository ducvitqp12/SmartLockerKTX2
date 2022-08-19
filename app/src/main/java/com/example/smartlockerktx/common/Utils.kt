package com.example.smartlockerktx.common

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter.formatIpAddress
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.MainActivity
import com.example.smartlockerktx.database.model.Employee
import com.example.smartlockerktx.repository.SmartLockerRepository
import com.example.smartlockerktx.service.EmployeeResponse
import com.example.smartlockerktx.service.GLEmpResponse
import com.example.smartlockerktx.service.LockerResponse
import com.example.smartlockerktx.service.SmartLockerAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val jsonObject: JSONObject = JSONObject().put("controllerDeviceImei", "1q2w3e4r5t6y")
val jsonObjectString = jsonObject.toString()
val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
lateinit var lockedEmployee : Array<String>
public var cur_page = 0

suspend fun saveEmployee(employeeResponse: EmployeeResponse, application: Application) {
    val smartLockerRepository = SmartLockerRepository(application)
    val subEmployees = employeeResponse.employees
    val accounts = employeeResponse.accounts
    for (i in 0 until subEmployees.size) {
        val employee = Employee(
            null, subEmployees[i].departmentID!!, subEmployees[i].employeeCode,
            subEmployees[i]._id!!, null, subEmployees[i].groupEmployeeId!!,
            subEmployees[i].name, subEmployees[i].pinCode, accounts[i].userName,
            accounts[i].password
        )
        smartLockerRepository.updateEmployee(employee)
    }

}

suspend fun saveGroupLockerEmployee(glEmpResponse: GLEmpResponse, application: Application) {
    val smartLockerRepository = SmartLockerRepository(application)
    val groupLockerEmployee = glEmpResponse.gles
    groupLockerEmployee.forEach {
        smartLockerRepository.updateGroupLockerEmployee(it)
    }

}

suspend fun saveLocker(lockerResponse: LockerResponse, application: Application) {
    val smartLockerRepository = SmartLockerRepository(application)
    val locker = lockerResponse.locker
    locker.forEach {
        smartLockerRepository.updateLocker(it)
    }
}

suspend fun saveAccess(access: Access, application: Application) {
    val smartLockerRepository = SmartLockerRepository(application)
    smartLockerRepository.updateAccess(access)
}



fun getEmployee(application: Application) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = SmartLockerAPI.retrofitService.getEmployee(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                launch(Dispatchers.IO) {
                    saveEmployee(response.body()!!, application)
                }
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}

fun getGles(application: Application) {
    CoroutineScope(Dispatchers.IO).launch {
        // Do the POST request and get response
        val response = SmartLockerAPI.retrofitService.getGle(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                launch(Dispatchers.IO) {
                    saveGroupLockerEmployee(response.body()!!, application)
                }
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}

fun getLocker(application: Application) {
    CoroutineScope(Dispatchers.IO).launch {
        // Do the POST request and get response
        val response = SmartLockerAPI.retrofitService.getLocker(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                launch(Dispatchers.IO) {
                    saveLocker(response.body()!!, application)
                }
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}

fun initAccessDB(application: Application) {
    CoroutineScope(Dispatchers.IO).launch {
        // Do the POST request and get response
        val smartLockerRepository = SmartLockerRepository(application)
        val response = SmartLockerAPI.retrofitService.getAccess(requestBody)
        lockedEmployee = arrayOf(String())
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                launch(Dispatchers.IO) {
                    val access = response.body()!!.access
                    Log.d("Sum", access.size.toString())
                    access.forEach {
                        saveAccess(it, application)

                        if(!(it.employeeId.isNullOrBlank())) {
                            val id = it.employeeId.toString()
                            if (!lockedEmployee.contains(id))
                                Log.d("Arraylist",lockedEmployee.indexOf(id).toString() + " " + id)
                                lockedEmployee += id
                        }

                        if(it.status == "SUSPEND"){
                            smartLockerRepository.updateLockerStatus(it.lockerId!!, 2)
                        }
                        if(it.status == "OCCUPIED"){
                            smartLockerRepository.updateLockerStatus(it.lockerId!!, 1)
                        }
                    }
                }
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}

//fun initDb(application: Application){
//    val smartLockerRepository = SmartLockerRepository(application)
//    getLocker(application)
//    getEmployee(application)
//    getGles(application)
//    initAccessDB(application)
//
//}

@RequiresApi(Build.VERSION_CODES.O)
fun curDateTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
   return  current.format(formatter)
}

fun remove(arr: Array<String>, id: String): Array<String> {
    val index = arr.indexOf(id)
    val result = arr.toMutableList()
    result.removeAt(index)
    return result.toTypedArray()
}