package com.ssrdi.co.id.myradboox.fragmentreseller


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ssrdi.co.id.myradboox.ExpiredActivity
import com.ssrdi.co.id.myradboox.LoginActivity
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.adapter.VoucherAdapter
import com.ssrdi.co.id.myradboox.api.Api
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.FragmentHomeBinding
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    lateinit var retro: Api
    lateinit var tokenLogin: String
    private lateinit var mAdapter: VoucherAdapter

    private lateinit var binding:FragmentHomeBinding
    private lateinit var layoutManager:LayoutManager
    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root:View = binding!!.root
        val list = resources.getStringArray(R.array.spinnerlist)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.list_item,list)
        binding.dropdownField.setAdapter(arrayAdapter)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        super.onViewCreated(view, savedInstanceState)

        val retro = RetrofitClient(requireContext())
            .getRetrofitClientInstance()
            .create(Api::class.java)

        retro.getVoucher("Bearer $tokenLogin").enqueue(object : Callback<VoucherResponse> {
            override fun onResponse(
                call: Call<VoucherResponse>,
                response: Response<VoucherResponse>
            ) {
//                val isiVoucher = response.body()!!.data
                val isiVoucher = response.body()
                val listHeroes = listOf(isiVoucher)
                if(response.isSuccessful){
//                    mAdapter = VoucherAdapter(listHeroes as List<VoucherResponse.DataObject>, context!!)
//                    val mLayoutManager = LinearLayoutManager(context)
//                    rvM.layoutManager = mLayoutManager
//                    rvM.itemAnimator = DefaultItemAnimator()
//                    rvM.adapter = mAdapter
                    val mAdapter = VoucherAdapter(listHeroes as List<VoucherResponse.DataObject>){ voucher ->
                            Toast.makeText(requireContext(), "hero clicked ${voucher.username}", Toast.LENGTH_SHORT).show()
                        }
                    rvM.apply {
                        //            layoutManager = GridLayoutManager(this@MainActivity, 3)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = mAdapter
                    }

                    //jika respon sukses disini
                } else if(response.code() == 406){
                    prosesLogout()
                }else if(response.code() ==402){
                    val intent= Intent(requireContext(), ExpiredActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })

            }

    override fun onResume() {
        super.onResume()


    }

    private fun prosesLogout(){
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




