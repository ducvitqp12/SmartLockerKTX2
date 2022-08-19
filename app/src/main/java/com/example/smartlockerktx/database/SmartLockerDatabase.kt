package com.example.smartlockerktx.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartlocker.database.model.Access
import com.example.smartlockerktx.database.DAO.*
import com.example.smartlockerktx.database.model.*


@Database(entities = [Access::class, AdminTag::class, Command::class, Employee:: class,
                     GroupLockerEmployee::class, History::class, Locker::class], version = 1)
abstract class SmartLockerDatabase: RoomDatabase() {
    abstract fun accessDAO(): AccessDAO
    abstract fun adminTagDAO(): AdminTagDAO
    abstract fun employeeDAO(): EmployeeDAO
    abstract fun groupLockerEmployeeDAO(): GroupLockerEmployeeDAO
    abstract fun lockerDAO(): LockerDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: SmartLockerDatabase
        fun getDatabase(context: Context): SmartLockerDatabase {
            synchronized(SmartLockerDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SmartLockerDatabase::class.java,
                        "smart_locker").build()
                }
            }
            return INSTANCE
        }

        fun getInstance() = INSTANCE
    }

}