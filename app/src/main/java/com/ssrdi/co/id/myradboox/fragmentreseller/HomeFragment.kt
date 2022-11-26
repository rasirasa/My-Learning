package com.ssrdi.co.id.myradboox.fragmentreseller


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssrdi.co.id.myradboox.ExpiredActivity
import com.ssrdi.co.id.myradboox.LoginActivity
import com.ssrdi.co.id.myradboox.adapter.VoucherAdapter
import com.ssrdi.co.id.myradboox.api.Api
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.FragmentHomeBinding
import com.ssrdi.co.id.myradboox.model.VoucherItemResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import com.ssrdi.co.id.myradboox.utility.PaginationScrollListener
import com.ssrdi.co.id.myradboox.utility.checkIfFragmentAttached
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    lateinit var retro: Api
    lateinit var tokenLogin: String

    private var page = 1
    private var tampilanPerItem = 20

    private lateinit var binding: FragmentHomeBinding

    private val PAGE_START = 0
    private var isLoading = false
    private var isLastPage = false

    private var currentPage: Int = PAGE_START

    // penampung data response dari backend
    private var voucherItemResponseAllData = mutableListOf<VoucherItemResponse?>()
    private var voucherItemChunk = listOf<List<VoucherItemResponse?>>()
    private var voucherItemPaging = mutableListOf<VoucherItemResponse?>()

    private lateinit var voucherAdapter: VoucherAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


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


        binding.rvM.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage() = isLastPage

            override fun isLoading() = isLoading

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                loadNextVoucher()
            }
        })
    }

    private fun loadNextVoucher() {
        val delay = 2_000L // 2 sec
        binding.loading.visibility = View.VISIBLE

        try {
            // delay 2sec
            Handler(Looper.getMainLooper()).postDelayed({

                voucherItemChunk[currentPage].map {
                    voucherItemPaging.add(it)
                    binding.rvM.post {
                        voucherAdapter.notifyItemInserted(voucherItemPaging.size - 1)
                    }
                }

                Log.d("debug", "load data page $currentPage")
                Log.d("debug", "size data paging ${voucherItemPaging.size}")

                isLoading = false
                binding.loading.visibility = View.GONE

                checkIfFragmentAttached {
                    Toast.makeText(requireContext(), "Sukses load next page", Toast.LENGTH_SHORT)
                        .show()
                }


            }, delay)

        } catch (e: Exception) {
            Log.e("debug", "error ${e.localizedMessage}")
        }
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

                        // ambil data item dibagi per 10
                        voucherItemChunk = voucherItemResponseAllData.chunked(10)

                        Log.d("debug", "jumlah chunk ${voucherItemChunk.size}")

                        // ambil data item yang sudah dibagi per 10, ambil by index 0
                        voucherItemPaging.addAll(voucherItemChunk[PAGE_START])

                        // update adapter
                        voucherAdapter.notifyDataSetChanged()

                    } else {
                        Log.e("debug", "api voucher error, response null")
                        checkIfFragmentAttached {
                            Toast.makeText(requireContext(), "Isi voucher null", Toast.LENGTH_SHORT)
                                .show()
                        }
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


