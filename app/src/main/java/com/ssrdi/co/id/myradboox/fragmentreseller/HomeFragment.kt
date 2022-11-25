package com.ssrdi.co.id.myradboox.fragmentreseller


import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

    //private lateinit var mAdapter: VoucherAdapter
    private lateinit var voucher: VoucherItemResponse
    private lateinit var binding: FragmentHomeBinding

    private lateinit var voucherAdapter: VoucherAdapter

    // penampung data response dari backend
    private var voucherItemResponse = mutableListOf<VoucherItemResponse?>()

    lateinit var itemsCells: MutableList<VoucherItemResponse?>
    lateinit var loadMoreItemsCells: List<VoucherItemResponse?>
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: LayoutManager

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
        voucherAdapter = VoucherAdapter(voucherItemResponse) {
            // set click listener
            Toast.makeText(requireContext(), "Ini hasil klik ${it.toString()}", Toast.LENGTH_SHORT)
                .show()
        }

        // buat layout manager untuk recyclerview
        mLayoutManager = LinearLayoutManager(requireContext())
        // set layout manager recyclerview
        binding.rvM.layoutManager = mLayoutManager

        // set adapter ke recyclerview
        binding.rvM.adapter = voucherAdapter

        // setup scroll listener buat recyclerview
        setRVScrollListener()
    }

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        binding.rvM.addOnScrollListener(scrollListener)
    }

    private fun loadMoreData() {
        //Add the Loading View
        voucherAdapter.addLoadingView()

        page++

        getVoucherPaging(page)

//        //Create the loadMoreItemsCells Arraylist
//        loadMoreItemsCells = mutableListOf<VoucherItemResponse?>()
//        //Get the number of the current Items of the main Arraylist
//        val start = voucherAdapter.itemCount
//        //Load 16 more items
//        val end = start + 16
//        //Use Handler if the items are loading too fast.
//        //If you remove it, the data will load so fast that you can't even see the LoadingView
//        Handler().postDelayed({
//            for (i in start..end) {
//                //Get data and add them to loadMoreItemsCells ArrayList
//                (loadMoreItemsCells as MutableList<VoucherItemResponse?>).add(null)
//            }
//            //Remove the Loading View
//            voucherAdapter.removeLoadingView()
//            //We adding the data to our main ArrayList
//
//            // TODO: gak tau ini mau ngapain
////            voucherAdapter.addData()
//
//            //Change the boolean isLoading to false
//            scrollListener.setLoaded()
//            //Update the recyclerView in the main thread
//            binding.rvM.post {
//                voucherAdapter.notifyDataSetChanged()
//            }
//        }, 3000)
    }

    private fun getVoucherPaging(page: Int) {
        Toast.makeText(requireContext(), "Panggil retrofit page $page", Toast.LENGTH_SHORT).show()
        // bikin fungsi retrofit untuk panggil page voucher selanjutnya
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

                        isiVoucher.data.map {
                            // log biar tau data nya ada apa ngga
                            Log.d("debug", "ini data response be -> ${it.toString()}")

                            // masukkan response voucher dari  be ke penampung
                            voucherItemResponse.add(it)
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


