package com.example.test.data.network

import retrofit2.Call
import retrofit2.http.GET


interface PingApi {
    @GET("/login/ping")
    fun ping(): Call<String>
}
