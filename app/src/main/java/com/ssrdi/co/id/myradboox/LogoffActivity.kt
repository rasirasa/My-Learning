package com.ssrdi.co.id.myradboox

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_logoff.*


class LogoffActivity : AppCompatActivity() {
//    var countDownTimer: CountDownTimer
//    val timerHasStarted = false
//    var btnStart: Button

//    val startTime = (30 * 1000).toLong()
//    val interval = (1 * 1000).toLong()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(com.ssrdi.co.id.myradboox.R.layout.activity_logoff)

//        textcounter = findViewById(com.ssrdi.co.id.myradboox.R.id.countdown) as TextView
//        countDownTimer = MyCountDownTimer(startTime, interval)
//        textcounter.text = textcounter.text.toString() + (startTime / 1000).toString()
//
//        countDownTimer.start();
        //var textcounter: TextView = findViewById(com.ssrdi.co.id.myradboox.R.id.countdown)
        var textcounter: TextView = findViewById(R.id.countdown)
        val timer = object: CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //var editText:EditText = findViewById(R.id.countdown)
                textcounter.setText("" + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                val intent = Intent(this@LogoffActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        timer.start()

//        Handler().postDelayed({
//            val intent = Intent(this@LogoffActivity, LoginActivity::class.java)
//            startActivity(intent)
//        },splashTimeOut)
    }

//    private class MyCountDownTimer(startTime: Long, interval: Long) :
//        CountDownTimer(startTime, interval) {
//
//        override fun onFinish() {
//            //val textcounter:TextView = findViewById(com.ssrdi.co.id.myradboox.R.id.countdown)
//            textcounter.setText("Time's up!")
//        }
//
//        override fun onTick(millisUntilFinished: Long) {
//            textcounter.setText("" + millisUntilFinished / 1000)
//        }
//    }
}