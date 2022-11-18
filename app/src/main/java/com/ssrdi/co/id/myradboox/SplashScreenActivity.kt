package com.ssrdi.co.id.myradboox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashTimeOut:Long = 3000
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        var role = SharedPrefManager.getInstance(this).role

        Handler().postDelayed({
            UserRoleUtils.checkUserRole(this@SplashScreenActivity, role)
            finish()
        },splashTimeOut)
    }
}