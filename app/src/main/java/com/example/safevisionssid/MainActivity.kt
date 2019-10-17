package com.example.safevisionssid

//import io.github.rybalkinsd.kohttp.ext.httpGet
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.rybalkinsd.kohttp.ext.httpGet
import java.lang.Exception




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
        val tv_ssid:TextView = findViewById(R.id.ssid_saved) as TextView
        val tv_pass:TextView = findViewById(R.id.password_saved) as TextView
        val tv_user:TextView = findViewById(R.id.user_saved) as TextView
        val button:Button = findViewById(R.id.submit) as Button

        button.setOnClickListener {
            var ssid = et_ssid.text.toString()
            var pass = et_pass.text.toString()
            var user = et_username.text.toString()
            var host = et_host.text.toString()
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
}
