package com.example.smartlockerktx.service

import com.google.gson.GsonBuilder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface APIService {

    @Headers("Accept:application/json; charset=utf-8", "Content-Type:application/json")
    @POST("/employee/GetEmployees")
    suspend fun getEmployee(@Body requestBody: RequestBody): Response<EmployeeResponse>

    @Headers("Accept:application/json; charset=utf-8", "Content-Type:application/json")
    @POST("/groupLockerEmployee/GetGroupLockerEmployees")
    suspend fun getGle(@Body requestBody: RequestBody): Response<GLEmpResponse>

    @Headers("Accept:application/json; charset=utf-8", "Content-Type:application/json")
    @POST("/locker/getAll")
    suspend fun getLocker(@Body requestBody: RequestBody): Response<LockerResponse>

    @Headers("Accept:application/json; charset=utf-8", "Content-Type:application/json")
    @POST("/access/getAccess")
    suspend fun getAccess(@Body requestBody: RequestBody): Response<AccessResponse>

    @Headers("Accept:application/json; charset=utf-8", "Content-Type:application/json")
    @PATCH("/access/update")
    suspend fun updateAccess(@Body requestBody: RequestBody): Response<ResponseBody>

    @Headers("Accept:application/json; charset=utf-8", "Content-Type:application/json")
    @POST("/history/add")
    suspend fun postHistory(@Body requestBody: RequestBody): Response<ResponseBody>



}

private val gson = GsonBuilder()
//    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .create()

//var baseUrl =  "http://192.168.100.6:3000"
var baseUrl =  "http://192.168.100.9:3000"

private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

object SmartLockerAPI {
    val retrofitService: APIService by lazy { retrofit.create(APIService::class.java) }
}