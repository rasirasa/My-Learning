package com.ssrdi.co.id.myradboox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssrdi.co.id.myradboox.api.Api
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.model.LoginRequest
import com.ssrdi.co.id.myradboox.model.LoginResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAction()

    }

    private fun initAction(){
        button_login.setOnClickListener {
            userLogin()
        }
    }
    private fun userLogin(){
        val username = q_username.text.toString().trim()
        val password = q_password.text.toString().trim()
        val request = LoginRequest()
        request.username = q_username.text.toString().trim()
        request.username = q_username.text.toString().trim()

        if(username.isEmpty()){
            q_username.error = "Email required"
            q_username.requestFocus()
            //return@setOnClickListener
        }

        if(password.isEmpty()){
            q_password.error = "Password required"
            q_username.requestFocus()
            //return@setOnClickListener
        }

        val retro = RetrofitClient(this).getRetrofitClientInstance().create(Api::class.java)
        retro.userLogin(username, password).enqueue(object :Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val user = response.body()
                val userStatusResponse = user?.status.toString()
                val userTokenResponse = user?.token.toString()
                if (userStatusResponse == "success") {
                    val intent = Intent(this@MainActivity, VerificationActivity::class.java)
                    intent.putExtra("token_login", userTokenResponse)
                    startActivity(intent)
//                    startActivity(Intent(this@MainActivity, VerificationActivity::class.java))
                } else {
                    Toast.makeText(applicationContext,"Username atau Password Tidak Cocock, Silahkan Ulangi",Toast.LENGTH_SHORT).show()
                }
//                if (user != null) {
//                    Log.e("token", user.token.toString())
//                    Log.e("phone", user.phone.toString())
//                    Log.e("status", user.status.toString())
//                    Log.e("message", user.message.toString())
//                }
           }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })

    }
}