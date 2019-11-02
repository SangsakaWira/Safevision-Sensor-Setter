package com.example.safevisionssid

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.github.rybalkinsd.kohttp.ext.httpGet


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val et_ssid:EditText = findViewById(R.id.ssid) as EditText
        val et_pass:EditText = findViewById(R.id.password) as EditText
        val et_username:EditText = findViewById(R.id.username) as EditText
        val et_host:EditText = findViewById(R.id.host) as EditText
        val et_sensor:EditText = findViewById(R.id.sensorname) as EditText
        val tv_ssid:TextView = findViewById(R.id.ssid_saved) as TextView
        val tv_pass:TextView = findViewById(R.id.password_saved) as TextView
        val tv_user:TextView = findViewById(R.id.user_saved) as TextView
//        val tv_sensor:Spinner = findViewById(R.id.sensorname) as Spinner
        val button:Button = findViewById(R.id.submit) as Button

        button.setOnClickListener {
            var ssid = et_ssid.text.toString()
            var pass = et_pass.text.toString()
            var user = et_username.text.toString()
            var host = et_host.text.toString()
//            var sensor = tv_sensor.text.toString()

            connectWifi("SUMIATI","90908080")
            tv_ssid.setText(ssid)
            tv_pass.setText(pass)
            tv_user.setText(user)
            println("SSID: "+ssid)
            println("Password: "+pass)
            println("Username: "+user)
            println("Host: "+host)
            try{
                setSSID(ssid,host)
                setPassword(pass,host)
                setUsername(user,host)
            }
            catch(e: Exception){
                val toast = Toast.makeText(applicationContext, "No Connection!", Toast.LENGTH_LONG)
                toast.show()
            }

            val toast = Toast.makeText(applicationContext, "New Data added!", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    fun setSSID(ssid:String,host:String) {
        println("http://"+host+"/"+"ssid/"+ssid)
        val response = ("http://"+host+"/"+"ssid/"+ssid).httpGet()
        println("")
    }

    fun setPassword(pass:String,host:String) {
        println("http://"+host+"/"+"ssid/"+pass)
        val response = ("http://"+host+"/"+"password/"+pass).httpGet()
    }

    fun setUsername(user:String,host:String) {
        println("http://"+host+"/"+"ssid/"+user)
        val response = ("http://"+host+"/"+"username/"+user).httpGet()
    }

    fun connectWifi(networkSSID:String,networkPass:String) {

        val conf = WifiConfiguration()
        conf.SSID = "\"" + networkSSID + "\""
        conf.preSharedKey = "\""+ networkPass +"\""
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.addNetwork(conf)

        val list = wifiManager.configuredNetworks
        for (i in list) {
            Log.d("ConnectWifi", i.SSID)
            if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                wifiManager.disconnect()
                wifiManager.enableNetwork(i.networkId, true)
                wifiManager.reconnect()
                break
            }
        }
    }

    fun funCheckStatus(host:String) {
        val local_response = ("http://"+host+"/"+"getAll/").httpGet()
        val server_response= ("http://"+host).httpGet()
        val local_body = local_response.body()
        val server_body = local_response.body()
    }

}

