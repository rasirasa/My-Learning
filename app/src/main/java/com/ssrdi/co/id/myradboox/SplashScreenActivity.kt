package com.ssrdi.co.id.myradboox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashTimeOut:Long = 3000
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        // val role = SharedPrefManager.Companion.getInstance("token")

        Handler().postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },splashTimeOut)
    }
}