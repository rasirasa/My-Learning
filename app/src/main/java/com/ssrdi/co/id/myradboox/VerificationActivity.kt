package com.ssrdi.co.id.myradboox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.model.DetailResponse
import com.ssrdi.co.id.myradboox.model.VerificationResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationActivity : AppCompatActivity() {

    lateinit var retro: RadbooxApi
    lateinit var tokenFromLogin: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        tokenFromLogin = intent.getStringExtra("token_login") ?: ""


        retro = RetrofitClient.getInstance(this)


//        testAmbilRoleUser()

        btn_verification.setOnClickListener {
            val codeOtpStr: String = ed_verification.text.toString().trim()


            // pertama cek dulu otp nya sudah diisi user belum
            if (codeOtpStr.isEmpty()) {
                ed_verification.error = "Kode OTP Wajib Diisi"
                ed_verification.requestFocus()
                Toast.makeText(this, "OTP kosong, mohon isi otp", Toast.LENGTH_SHORT).show()
            } else {
                ed_verification.error = null
                val codeOtpInt = codeOtpStr.toInt()
                prosesOtp(codeOtpInt)
            }

        }

    }

    private fun testAmbilRoleUser() {
        var role = SharedPrefManager.getInstance(this).role
        Log.d("test", "role user saat ini $role")
        Toast.makeText(this, "Role saat ini -> $role", Toast.LENGTH_SHORT).show()

        // ubah data role ke admin
//        SharedPrefManager.getInstance(this).role = "admin"
//        role = SharedPrefManager.getInstance(this).role
//        Log.d("test","role user saat ini $role")
//        Toast.makeText(this, "Role saat ini -> $role", Toast.LENGTH_SHORT).show()
    }

    /**
     * proses otp dengan mengirim ke API dengan retrofit
     */
    private fun prosesOtp(codeOtpInt: Int) {

        loading.visibility = View.VISIBLE

        retro.loginVerification(codeOtpInt, "Bearer $tokenFromLogin")
            .enqueue(object : Callback<VerificationResponse> {
                override fun onResponse(
                    call: Call<VerificationResponse>,
                    response: Response<VerificationResponse>
                ) {

                    loading.visibility = View.GONE
                    if (response.isSuccessful) {
//
                        val loginVerif = response.body()
                        val loginStatusResponse = loginVerif?.status.toString()
                        val loginTokenResponse = loginVerif?.token.toString()
//                            if (loginStatusResponse == "success") {

                        SharedPrefManager.getInstance(applicationContext)
                            .saveToken(loginVerif?.token.toString())

                        ambilDetailAdmin(loginTokenResponse)


//  NANTI DISINI PEMBAGIAN ROLE AWAL --- BYPASS LANGSUNG KE RESELLERACTIVITY
                        val intent =
                            Intent(this@VerificationActivity, ResellerActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)

                    } else {
                        Log.e("tag", "error gak sukses di login")
                        Toast.makeText(
                            this@VerificationActivity,
                            "Verif failed ${response.isSuccessful}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    // batas fungsi


                }

                override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                    Log.e("tag", "gagal response")
                    loading.visibility = View.GONE
                }
            })


    }

    private fun ambilDetailAdmin(loginTokenResponse: String) {
        //  ambil data detail
        retro.detailAdmin("Bearer $loginTokenResponse").enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                val detailAdminnya = response.body()
                if (detailAdminnya != null) {
                    SharedPrefManager.getInstance(applicationContext)
                        .saveDetailAdmin(detailAdminnya)
                }

            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}


