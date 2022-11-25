package com.ssrdi.co.id.myradboox.fragmentreseller


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ssrdi.co.id.myradboox.ExpiredActivity
import com.ssrdi.co.id.myradboox.LoginActivity
import com.ssrdi.co.id.myradboox.adapter.VoucherAdapter
import com.ssrdi.co.id.myradboox.api.Api
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.FragmentHomeBinding
import com.ssrdi.co.id.myradboox.model.VoucherItemResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.readmore.OnLoadMoreListener
import com.ssrdi.co.id.myradboox.readmore.RecyclerViewLoadMoreScroll
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    lateinit var retro: Api
    lateinit var tokenLogin: String

    private var page = 1
    private var tampilanPerItem = 20

    private lateinit var binding: FragmentHomeBinding

    private lateinit var voucherAdapter: VoucherAdapter

    // penampung data response dari backend
    private var voucherItemResponseAllData = mutableListOf<VoucherItemResponse?>()
    private var voucherItemPaging = mutableListOf<VoucherItemResponse?>()

    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var linearLayoutManager: LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // sop disini
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // ambil token dari shared pref
        tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        // setup config buat recyclerview
        setupRecyclerView()

        // buat object retrofit
        retro = RetrofitClient(requireContext())
            .getRetrofitClientInstance()
            .create(Api::class.java)

        // panggil api voucher
        getVoucher()
    }

    private fun setupRecyclerView() {
        // create adapter
        voucherAdapter = VoucherAdapter(voucherItemPaging) {
            // set click listener
            Toast.makeText(requireContext(), "Ini hasil klik ${it.toString()}", Toast.LENGTH_SHORT)
                .show()
        }

        // buat layout manager untuk recyclerview
        linearLayoutManager = LinearLayoutManager(requireContext())
        // set layout manager recyclerview
        binding.rvM.layoutManager = linearLayoutManager

        // set adapter ke recyclerview
        binding.rvM.adapter = voucherAdapter

        // setup scroll listener buat recyclerview
        setRVScrollListener()
    }

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(linearLayoutManager as LinearLayoutManager)

        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                Log.d("debug", "on loadmore")
                page++
                getVoucherPaging(page)
            }
        })
        binding.rvM.addOnScrollListener(scrollListener)
    }

    private fun getVoucherPaging(page: Int) {
        val ambilData = page * tampilanPerItem

        Log.d("debug", "get voucher page $page ambilData $ambilData")

        val displayedList = voucherItemResponseAllData.take(ambilData)
        voucherItemPaging.clear()
        displayedList.map {
            voucherItemPaging.add(it)
        }
        Log.d("debug", "jumlah voucher item paging ${voucherItemPaging.size}")

        voucherAdapter.notifyDataSetChanged()
    }


    private fun getVoucher() {
        Log.d("debug", "panggil api voucher")
        retro.getVoucher("Bearer $tokenLogin").enqueue(object : Callback<VoucherResponse> {
            override fun onResponse(
                call: Call<VoucherResponse>,
                response: Response<VoucherResponse>
            ) {
                if (response.isSuccessful) {

                    val isiVoucher = response.body()

                    if (isiVoucher != null) {

                        Log.d("debug", "jumlah data server ${isiVoucher.data.size}")

                        isiVoucher.data.map {
                            // log biar tau data nya ada apa ngga
                            // masukkan response voucher dari  be ke penampung
                            voucherItemResponseAllData.add(it)
                        }

                        // ambil 10 voucher pertama
                        voucherItemResponseAllData.take(tampilanPerItem).map {
                            voucherItemPaging.add(it)
                        }

                        // kasih tau adapter kalo ada data baru, biar muncul data barunya
                        voucherAdapter.notifyDataSetChanged()

                    } else {
                        Log.e("debug", "api voucher error, response null")
                        Toast.makeText(requireContext(), "Isi voucher null", Toast.LENGTH_SHORT)
                            .show()
                    }


                    //jika respon sukses disini
                } else if (response.code() == 406) {
                    Log.e("debug", "api voucher error, response 406")
                    Toast.makeText(requireContext(), "error 406", Toast.LENGTH_SHORT).show()
                    prosesLogout()
                } else if (response.code() == 402) {
                    Log.e("debug", "api voucher error, response 406")
                    Toast.makeText(requireContext(), "error 402", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), ExpiredActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                Log.e("Error", "error " + t.message.toString())
            }

        })
    }


    private fun prosesLogout() {
        var preference = SharedPrefManager.getInstance(requireContext())
        preference.clearAll()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}


