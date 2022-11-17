package com.ssrdi.co.id.myradboox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssrdi.co.id.myradboox.api.Api
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.model.VerificationResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        var codeOtpStr = ed_verification.text.toString().trim()
        val codeOtpInt = codeOtpStr.toInt()
        btn_verification.setOnClickListener {
            // pertama cek dulu otp nya sudah diisi user belum
            if (codeOtpStr.isEmpty()) {

                ed_verification.error = "Kode OTP Wajib Diisi"
                ed_verification.requestFocus()
                Toast.makeText(this, "OTP kosong, mohon isi otp", Toast.LENGTH_SHORT).show()
            }else {
                ed_verification.error = ""


                prosesOtp(codeOtpInt)
            }

        }

        /**
         * proses otp dengan mengirim ke API dengan retrofit
         */


//            retro.loginVerification(codeOtp.text.toString().toInt(), "Bearer $tokenFromLogin")
//                .enqueue(object : Callback<VerificationResponse> {
//                    override fun onResponse(
//                        call: Call<VerificationResponse>,
//                        response: Response<VerificationResponse>
//                    ) {
//                        if (response.isSuccessful) {
////
//                            val loginVerif = response.body()
//                            val loginStatusResponse = loginVerif?.status.toString()
//                            val loginTokenResponse = loginVerif?.token.toString()
//                            if (loginStatusResponse == "success") {
//
//                                SharedPrefManager.getInstance(applicationContext)
//                                    .saveToken(loginVerif?.token.toString())
//
////  ambil data detail
////                                retro.detailAdmin("Bearer $loginTokenResponse").enqueue(object: Callback<DetailResponse>{
////                                    override fun onResponse(
////                                        call: Call<DetailResponse>,
////                                        response: Response<DetailResponse>
////                                    ) {
////                                        TODO("Not yet implemented")
////                                    }
////
////                                    override fun onFailure(
////                                        call: Call<DetailResponse>,
////                                        t: Throwable
////                                    ) {
////                                        TODO("Not yet implemented")
////                                    }
////
////                                })
//
////  akhir data detail
//                                val intent =
//                                    Intent(this@VerificationActivity, ResellerActivity::class.java)
//                                intent.flags =
//                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//                                startActivity(intent)
//
//                            } else {
//                                Log.e("tag", "error gak sukses di login")
//                            }
//
//                        } else {
//                            Log.e("tag", "error gak sukses")
//                        }
//                    }
//                    // batas fungsi
//
//
//                    override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
//                        TODO("Not yet implemented")
//                    }
//
//                })
//            if (codeOtpok.isEmpty()) {
//                ed_verification.error = "Kode OTP Wajib Diisi"
//                ed_verification.requestFocus()
//                //return@setOnClickListener
//            }
//            val retro = RetrofitClient().getRetrofitClientInstance().create(Api::class.java)
//            retro.loginVerification(codeOtp,tokenFromLogin).enqueue(object : Callback<VerificationResponse>{
//                override fun onResponse(
//                    call: Call<VerificationResponse>,
//                    response: Response<VerificationResponse>
//                ) {
//                    val loginVerif = response.body()
//                    val loginStatusResponse = loginVerif?.status.toString()
//                    val loginTokenResponse = loginVerif?.token.toString()
//                    if(loginStatusResponse == "success"){
//
//                        SharedPrefManager.getInstance(applicationContext).saveToken(loginVerif?.token.toString())
//
//                        val intent = Intent(applicationContext, ResellerActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//                        startActivity(intent)
//
//
//                    }else{
//                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
//                    }
//
//                }
//
//                override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }
//    }
    }
    private fun prosesOtp(codeOtpInt: Int) {
        val retro = RetrofitClient(this)
            .getRetrofitClientInstance()
            .create(Api::class.java)
        val tokenFromLogin= getIntent().getStringExtra("token_login")

        retro.loginVerification(codeOtpInt, "Bearer $tokenFromLogin")
            .enqueue(object : Callback<VerificationResponse> {
                override fun onResponse(
                    call: Call<VerificationResponse>,
                    response: Response<VerificationResponse>
                ) {
                    if (response.isSuccessful) {
//
                        val loginVerif = response.body()
                        val loginStatusResponse = loginVerif?.status.toString()
                        val loginTokenResponse = loginVerif?.token.toString()
//                            if (loginStatusResponse == "success") {

                        SharedPrefManager.getInstance(applicationContext)
                            .saveToken(loginVerif?.token.toString())

//  ambil data detail
//                                retro.detailAdmin("Bearer $loginTokenResponse").enqueue(object: Callback<DetailResponse>{
//                                    override fun onResponse(
//                                        call: Call<DetailResponse>,
//                                        response: Response<DetailResponse>
//                                    ) {
//                                        TODO("Not yet implemented")
//                                    }
//
//                                    override fun onFailure(
//                                        call: Call<DetailResponse>,
//                                        t: Throwable
//                                    ) {
//                                        TODO("Not yet implemented")
//                                    }
//
//                                })

//  akhir data detail
                        val intent =
                            Intent(this@VerificationActivity, ResellerActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)

                    } else {
                        Log.e("tag", "error gak sukses di login")
                    }


                    // batas fungsi



                }
                override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                    Log.e("tag", "gagal response")
                }
            })


    }
}


