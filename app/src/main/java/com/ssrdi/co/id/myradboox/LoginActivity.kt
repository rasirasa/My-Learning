package com.ssrdi.co.id.myradboox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.model.LoginRequest
import com.ssrdi.co.id.myradboox.model.LoginResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_session.*
import kotlinx.android.synthetic.main.login_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class LoginActivity : AppCompatActivity() {

    private var isRememberMechecked = false
    private lateinit var pref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        pref = SharedPrefManager.getInstance(this)

        initAction()

        if (BuildConfig.DEBUG) {
            q_username.setText("rahmat")
            q_password.setText("rahmat")
        }

        // check jika user rememberpassword
        val pairUserPass = pref.getUserPassword()
        if (pairUserPass.first.isNotBlank()) {
            q_username.setText(pairUserPass.first)
            q_password.setText(pairUserPass.second)
        }

        rememberme.setOnCheckedChangeListener { _, isChecked ->
            isRememberMechecked = true
        }
    }

    private fun saveRememberMe() {
        pref.saveUserPassword(q_username.text.toString(), q_password.text.toString())
    }

    private fun initAction() {
        val userLogin = SharedPrefManager.getInstance(this).userLogin
        val passLogin = SharedPrefManager.getInstance(this).passLogin
        if (userLogin != null && passLogin != null) {
            q_username.setText(userLogin)
            q_password.setText(passLogin)
        }
        var counter: Int = 0
        button_login.setOnClickListener {
            userLogin()
            counter++
            if (counter >= 6) {
                logOff()
            }

        }
    }

    private fun userLogin() {
        val username = q_username.text.toString().trim()
        val password = q_password.text.toString().trim()
        val request = LoginRequest()
        request.username = q_username.text.toString().trim()
        request.password = q_password.text.toString().trim()

        loading.visibility = View.VISIBLE

        if (username.isEmpty()) {
            q_username.error = "Email required"
            q_username.requestFocus()
        }

        if (password.isEmpty()) {
            q_password.error = "Password required"
            q_password.requestFocus()
        }

        val retro = RetrofitClient.getInstance(this)

        retro.userLogin(username, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                loading.visibility = View.GONE

                val user = response.body()
                val userStatusResponse = user?.status.toString()
                val userTokenResponse = user?.token.toString()
                if (userStatusResponse == "success") {

                    if (isRememberMechecked) {
                        saveRememberMe()
                    }
                    val intent = Intent(this@LoginActivity, VerificationActivity::class.java)
                    intent.putExtra("token_login", userTokenResponse)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Username atau Password Tidak Cocock, Silahkan Ulangi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Timber.e("Error", "error  ${t.localizedMessage}")
            }

        })
    }

    private fun logOff() {
        val intent = Intent(this@LoginActivity, LogoffActivity::class.java)
        startActivity(intent)
    }
}