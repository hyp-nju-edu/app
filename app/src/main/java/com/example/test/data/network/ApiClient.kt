package com.example.test.data.network
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {
    // README 里的默认服务地址
    private const val BASE_URL = "http://10.6.86.86/"

    val pingApi: PingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(PingApi::class.java)
    }
}