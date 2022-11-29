package com.ssrdi.co.id.myradboox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_detail_voucher.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVoucherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_voucher)

        val voucherId = intent.getIntExtra("voucher_id", 0)

        getVoucherById(voucherId)

        btnPrint.setOnClickListener {
            Toast.makeText(this, "Btn Print", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getVoucherById(voucherId: Int) {
        val api = RetrofitClient.getInstance(this)
        val // ambil token dari shared pref
                tokenLogin = SharedPrefManager.getInstance(this).tokenLogin

        api.getVoucherById("Bearer $tokenLogin", voucherId)
            .enqueue(object : Callback<VoucherResponse> {
                override fun onResponse(
                    call: Call<VoucherResponse>,
                    response: Response<VoucherResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.d("debug", "response -> ${data?.data}")

                        if (data != null) {
                            val detailVoucher = data.data[0]

                            tv_profile.setText("Profile : ${detailVoucher.profile}")
                            tv_status.setText("Status : ${detailVoucher.status}")

                            tv_username.setText(detailVoucher.username)
                            tv_start_time_value.setText(detailVoucher.start_time)
                            tv_end_time_value.setText(detailVoucher.end_time)

                        }


                    } else {
                        Toast.makeText(
                            this@DetailVoucherActivity,
                            "Response gagal",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }


                override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                    Log.e("debug", "error -> ${t.localizedMessage}")
                }


            })

    }
}