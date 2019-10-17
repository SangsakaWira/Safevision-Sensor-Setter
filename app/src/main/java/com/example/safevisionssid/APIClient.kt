package com.example.safevisionssid

import retrofit2.http.GET

interface APIClient {


    @GET("ssid/{ssid}")
    fun getAllVideo()

    @GET("password/{pass}")
    fun getMateriVideo()

    @GET("username/{user}")
    fun getBendaVideo()

}