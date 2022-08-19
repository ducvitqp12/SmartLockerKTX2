package com.example.smartlockerktx.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.MainActivity
import com.example.smartlockerktx.common.*
import com.example.smartlockerktx.database.SmartLockerDatabase
import com.example.smartlockerktx.database.model.Employee
import com.example.smartlockerktx.database.model.Locker
import com.example.smartlockerktx.repository.SmartLockerRepository
import com.example.smartlockerktx.service.SmartLockerAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val smartLockerRepository = SmartLockerRepository(application)
    private val _lockers = MutableLiveData<List<Locker>>()
    private val _employees = MutableLiveData<List<Employee>>()
    private val _employeeLocker = MutableLiveData<List<Locker>>()
    private val _lockerUser = MutableLiveData<Employee>()


    val lockerUser: LiveData<Employee>
        get() = _lockerUser

    val lockers: LiveData<List<Locker>>
        get() = _lockers

    val employees: LiveData<List<Employee>>
        get() = _employees

    val employeeLocker: LiveData<List<Locker>>
        get() = _employeeLocker


    fun displayLocker(){

        viewModelScope.launch {

            try {
                _lockers.value = smartLockerRepository.getLocker(cur_page+1)
                Log.d("Abcd", "display " + _lockers.value?.size.toString())
            }
            catch (e: Exception){
                e.printStackTrace()
            }

        }
        init()
    }

    private fun init(){
        viewModelScope.launch {

            try {
//                smartLockerRepository.deleteEmployeeTable()
                _employees.value = smartLockerRepository.getEmployee()
                Log.d("Abcd", "init" +  _employees.value?.size.toString())
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun startUp(application: Application){
        getLocker(application)
        getEmployee(application)
        getGles(application)
        initAccessDB(application)
        displayLocker()
    }
    fun updateLockerStatus(lockerId: String, status: Int){
        viewModelScope.launch {

            try {
                smartLockerRepository.updateLockerStatus(lockerId, status)
                displayLocker()
            }
            catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
    fun getEmployeeLocker(groupEmployeeId: String){
        viewModelScope.launch {
            try {
                displayLocker()
                _employeeLocker.value = smartLockerRepository.getEmployeeLockers(groupEmployeeId)
                Log.d("Abcd", "Employee Locker" + _employeeLocker.value?.size.toString())
            }
            catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun updateAccess(lockerId: String, accessTime: String, status: String, employeeId: String = "current"){
        viewModelScope.launch {
            try {
                val access = smartLockerRepository.getAccess(lockerId)
                if (employeeId == "current")
                    smartLockerRepository.updateAccess(lockerId, accessTime, status, smartLockerRepository.getAccess(lockerId).employeeId.toString())
                else smartLockerRepository.updateAccess(lockerId, accessTime, status, employeeId)
                if (status == "FREE"){
                    lockedEmployee = remove(lockedEmployee, access.employeeId.toString())
                }
                SmartLockerAPI.retrofitService.updateAccess(smartLockerRepository.getAccess(lockerId).toString().toRequestBody("application/json".toMediaTypeOrNull()))
                SmartLockerAPI.retrofitService.postHistory(smartLockerRepository.getAccess(lockerId).toString().toRequestBody("application/json".toMediaTypeOrNull()))
            }
            catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun getLockerUser(lockerId: String) {
        viewModelScope.launch {
            try {
                Log.d("Test","Start")
                val access = smartLockerRepository.db.accessDAO().getAccessByLockerId(lockerId)
                Log.d("Test","Done 1")
                _lockerUser.value = smartLockerRepository.db.employeeDAO().getEmployee(access.employeeId.toString())
                Log.d("Test",_lockerUser.value!!.pinCode!!)
            }
            catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun reset(){
        _employeeLocker.value
    }

    override fun onCleared() {
        super.onCleared()
        // Dispose All your Subscriptions to avoid memory leaks
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SharedViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

