package com.example.smartlockerktx.common

import android.app.Application
import com.example.smartlockerktx.database.SmartLockerDatabase

class SmartLockerApplication: Application() {
    val database:SmartLockerDatabase by lazy { SmartLockerDatabase.getDatabase(this) }
}