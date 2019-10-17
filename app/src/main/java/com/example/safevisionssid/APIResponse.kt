package com.example.safevisionssid

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIResponse {

    val url = "http://157.245.203.236:4500/"
    fun response():APIClient{
        val client = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return client.create(APIClient::class.java)
    }

}