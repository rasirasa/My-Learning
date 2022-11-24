package com.ssrdi.co.id.myradboox.fragmentreseller


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
<<<<<<< HEAD
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DefaultItemAnimator
=======
>>>>>>> 6f6c8647f8044e8d510be49f2b086dc627972d73
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ssrdi.co.id.myradboox.ExpiredActivity
import com.ssrdi.co.id.myradboox.LoginActivity
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.adapter.VoucherAdapter
import com.ssrdi.co.id.myradboox.api.Api
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.FragmentHomeBinding
import com.ssrdi.co.id.myradboox.model.VoucherItemResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    lateinit var retro: Api
    lateinit var tokenLogin: String

    private lateinit var binding: FragmentHomeBinding
<<<<<<< HEAD
    private lateinit var layoutManager:LayoutManager
=======

    private lateinit var mAdapter: VoucherAdapter
    private lateinit var layoutManager: LayoutManager
>>>>>>> 6f6c8647f8044e8d510be49f2b086dc627972d73
    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
<<<<<<< HEAD
        return inflater.inflate(R.layout.fragment_home, container, false)
//        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root:View = binding!!.root
//        val list = resources.getStringArray(R.array.spinnerlist)
//        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.list_item,list)
//        binding.dropdownField.setAdapter(arrayAdapter)
//        return root
=======
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
>>>>>>> 6f6c8647f8044e8d510be49f2b086dc627972d73
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        // ambil data list dari resource string
        val list = resources.getStringArray(R.array.spinnerlist)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, list)
        // tampilkan ke dropdown
        binding.dropdownField.setAdapter(arrayAdapter)

<<<<<<< HEAD
=======

        // buat client retrofit
>>>>>>> 6f6c8647f8044e8d510be49f2b086dc627972d73
        retro = RetrofitClient(requireContext())
            .getRetrofitClientInstance()
            .create(Api::class.java)

        retro.getVoucher("Bearer $tokenLogin").enqueue(object : Callback<VoucherResponse> {
            override fun onResponse(
                call: Call<VoucherResponse>,
                response: Response<VoucherResponse>
            ) {
<<<<<<< HEAD
//                val isiVoucher = response.body()!!.data
                val isiVoucher = response.body()!!.data
                val listHeroes = listOf(isiVoucher)
                if(response.isSuccessful){
                    val mAdapter = VoucherAdapter(isiVoucher){ voucher ->
//                        val options = navOptions {
//                            anim {
//                                enter = R.anim.slide_in_right
//                                exit = R.anim.slide_out_left
//                                popEnter = R.anim.slide_in_left
//                                popExit = R.anim.slide_out_right
//                            }
//                        }
                        findNavController().navigate(R.id.detailVoucherFragment,null)
//                            Toast.makeText(requireContext(), "hero clicked ${voucher.username}", Toast.LENGTH_SHORT).show()
=======

                if (response.isSuccessful) {
                    // ambil data dari response
                    val voucherResponse = response.body()

                    // ini coba debugging
                    Log.d("debug", "response get voucher success ${response.toString()}")

                    // check dulu voucher response null apa nggak
                    if (voucherResponse != null) {
                        val mAdapter =
                            VoucherAdapter(voucherResponse.data) { voucher ->
                                Toast.makeText(
                                    requireContext(),
                                    "voucher  clicked ${voucher.username}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        rvM.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = mAdapter
>>>>>>> 6f6c8647f8044e8d510be49f2b086dc627972d73
                        }
                    }


                    //jika respon sukses disini
                } else if (response.code() == 406) {
                    Log.d("debug", "response get voucher failed 406")
                    prosesLogout()
                } else if (response.code() == 402) {
                    Log.d("debug", "response get voucher failed 402")
                    val intent = Intent(requireContext(), ExpiredActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })

    }


    private fun prosesLogout() {
        var preference = SharedPrefManager.getInstance(requireContext())
        preference.clearAll()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)

        finish()
    }


    private fun finish() {
        TODO("Not yet implemented")
    }

    class Hero(
        val name: String, val image: String
    )
}




