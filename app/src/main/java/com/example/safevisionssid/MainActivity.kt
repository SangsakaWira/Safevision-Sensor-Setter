package com.example.safevisionssid

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.github.rybalkinsd.kohttp.ext.httpGet
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        const val HOST = "192.168.4.1:3000"
    }

    val et_ssid by lazy { findViewById<EditText>(R.id.ssid) }
    val et_pass by lazy { findViewById<EditText>(R.id.password) }
    val et_username by lazy { findViewById<EditText>(R.id.username) }
    val et_sensor by lazy { findViewById<EditText>(R.id.sensorname) }
    val tv_ssid by lazy { findViewById<TextView>(R.id.ssid_saved) }
    val tv_pass by lazy { findViewById<TextView>(R.id.password_saved) }
    val tv_user by lazy { findViewById<TextView>(R.id.user_saved) }
    val button by lazy { findViewById<Button>(R.id.submit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        button.setOnClickListener {
            val ssid = et_ssid.text.toString()
            val pass = et_pass.text.toString()
            val user = et_username.text.toString()
            val sensor = et_sensor.text.toString()
            val passwordAP = "safevision"

            try {
                connectWifi(sensor, passwordAP)

                Toast.makeText(applicationContext, "Connected!", Toast.LENGTH_LONG).show()

                checkInfo()

                try {
                    setSSID(ssid, HOST)
                    setPassword(pass, HOST)
                    setUsername(user, HOST)
                } catch (e: Exception) {
                    println(e)
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                println(e)
                Toast.makeText(applicationContext, "No Connection!", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun setSSID(ssid: String, host: String) {
        val response = ("http://$host/ssid/$ssid").httpGet()
    }

    fun setPassword(pass: String, host: String) {
        val response = ("http://$host/pass/$pass").httpGet()
    }

    fun setUsername(user: String, host: String) {
        val response = ("http://$host/userid/$user").httpGet()
    }

    fun reset(host: String) {
        val response = ("http://$host/restart/").httpGet()
    }

    fun connectWifi(networkSSID: String, networkPass: String) {
        val conf = WifiConfiguration()
        conf.SSID = "\"" + networkSSID + "\""
        conf.preSharedKey = "\"" + networkPass + "\""
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

    fun funCheckStatus(host: String): JSONObject {
        val response = ("http://$host/jsoninfo/").httpGet()
        return JSONObject(response.body()?.string())
    }

    fun checkInfo() {
        println("CHECKING INFO")

        MainProcess {
            tv_user.text = it?.getString("user_id")
            tv_pass.text = it?.getString("pass")
            tv_ssid.text = it?.getString("ssid")
        }.execute({
            var counter = 0
            var result: JSONObject? = null
            val maxCounter = 30

            while (true) {
                try {
                    funCheckStatus(HOST)

                    setSSID(et_ssid.text.toString(), HOST)
                    setPassword(et_pass.text.toString(), HOST)
                    setUsername(et_username.text.toString(), HOST)

                    result = funCheckStatus(HOST)

                    println("SUCCESS CHECK INFO")

                    break
                } catch (e: Exception) {
                    println("ERROR CHECK INFO")
                    println(e.message)

                    if (counter > maxCounter) break
                    counter++
                    Thread.sleep(1000)
                }
            }

            return@execute result
        })
    }

    class MainProcess(private val callback: (result: JSONObject?) -> Unit) :
        AsyncTask<() -> JSONObject?, Unit, JSONObject?>() {

        override fun doInBackground(vararg params: (() -> JSONObject?)): JSONObject? {
            println("RUNNING CHECK INFO PROCESS")
            return params[0]()
        }

        override fun onPostExecute(result: JSONObject?) {
            callback(result)
        }

    }

}



