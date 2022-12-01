package com.ssrdi.co.id.myradboox.fragmentreseller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.FragmentDetailVoucherBinding
import com.ssrdi.co.id.myradboox.databinding.FragmentHomeBinding
import com.ssrdi.co.id.myradboox.model.VoucherItemDetailResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponseDetail
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import com.ssrdi.co.id.myradboox.utility.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail_voucher.*
import kotlinx.android.synthetic.main.item_hero.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVoucherFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentDetailVoucherBinding
    lateinit var retro: RadbooxApi

    lateinit var tokenLogin: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_detail_voucher, container, false)


        binding = FragmentDetailVoucherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var idfromdetail: Int
        super.onViewCreated(view, savedInstanceState)


//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        findViewById<Toolbar>(R.id.toolbar)
//            .setupWithNavController(navController, appBarConfiguration)

        val args = this.arguments
        val idDetail: String = args?.get("id") as String

        // buat object retrofit
        retro = RetrofitClient.getInstance(requireContext())

        // panggil api voucher
        getDetailVoucher(idDetail)

        btn_print.setOnClickListener {
            Toast.makeText(requireContext(), "Btn Print", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getDetailVoucher(idDetail: String) {

        tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        retro.getVoucherById("Bearer $tokenLogin", idDetail.toInt())
            .enqueue(object : Callback<VoucherResponse> {
                override fun onResponse(
                    call: Call<VoucherResponse>,
                    response: Response<VoucherResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.d("debug", "Respons->${data?.data}")

                        if (data != null) {
                            val detailVoucher = data.data[0]
                            txt_username1.text = detailVoucher.username
                            txt_Profile.text = detailVoucher.profile
                            txt_Status.text = detailVoucher.status
                            txt_CreateFirstTime.text = detailVoucher.create_time
                            txt_StartTime.text = detailVoucher.start_time
                            txt_EndTime.text = detailVoucher.end_time
                        }
                    } else {
                        Toast.makeText(
                            requireContext(), "Response gagal", Toast.LENGTH_SHORT
                        ).show()
                    }


                }

                override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                    Log.e("debug", "error -> ${t.localizedMessage}")
                }

            })
    }


}


