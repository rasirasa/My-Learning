package com.ssrdi.co.id.myradboox.fragmentreseller


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.ssrdi.co.id.myradboox.readmore.OnLoadMoreListener
import com.ssrdi.co.id.myradboox.readmore.RecyclerViewLoadMoreScroll
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    lateinit var retro: Api
    lateinit var tokenLogin: String
    //private lateinit var mAdapter: VoucherAdapter
    private lateinit var voucher: VoucherItemResponse
    private lateinit var binding: FragmentHomeBinding
    lateinit var itemsCells: MutableList<VoucherItemResponse?>
    lateinit var loadMoreItemsCells: List<VoucherItemResponse?>
    lateinit var adapterLinear: VoucherAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//       return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root:View = binding!!.root
//        val list = resources.getStringArray(R.array.spinnerlist)
//        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.list_item,list)
//        binding.dropdownField.setAdapter(arrayAdapter)
        //** Set the data for our ArrayList
        setItemsData()

        //** Set the adapterLinear of the RecyclerView
        setAdapter()

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        //** Set the scrollListener of the RecyclerView
        setRVScrollListener()
        return binding.root
    }
// go function
private fun setItemsData() {
    itemsCells = mutableListOf<VoucherItemResponse?>()
    for (i in 0..40) {
        itemsCells.add(null)
    }
}

    private fun setAdapter() {
        loadMoreItemsCells = mutableListOf<VoucherItemResponse?>()
        adapterLinear = VoucherAdapter(itemsCells, loadMoreItemsCells)
        adapterLinear.notifyDataSetChanged()
        binding.rvM.adapter = adapterLinear
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(this)
        binding.rvM.layoutManager = mLayoutManager
        binding.rvM.setHasFixedSize(true)
    }

    private  fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(requireContext())
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
        adapterLinear.addLoadingView()
        //Create the loadMoreItemsCells Arraylist
        loadMoreItemsCells = mutableListOf<VoucherItemResponse?>()
        //Get the number of the current Items of the main Arraylist
        val start = adapterLinear.itemCount
        //Load 16 more items
        val end = start + 16
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Handler().postDelayed({
            for (i in start..end) {
                //Get data and add them to loadMoreItemsCells ArrayList
                (loadMoreItemsCells as MutableList<VoucherItemResponse?>).add(null)
            }
            //Remove the Loading View
            adapterLinear.removeLoadingView()
            //We adding the data to our main ArrayList
            adapterLinear.addData(mutableListOf<VoucherItemResponse?>())
            //Change the boolean isLoading to false
            scrollListener.setLoaded()
            //Update the recyclerView in the main thread
            binding.rvM.post {
                adapterLinear.notifyDataSetChanged()
            }
        }, 3000)
    }

// sop disini
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        super.onViewCreated(view, savedInstanceState)

        retro = RetrofitClient(requireContext())
            .getRetrofitClientInstance()
            .create(Api::class.java)

        retro.getVoucher("Bearer $tokenLogin").enqueue(object : Callback<VoucherResponse> {
            override fun onResponse(
                call: Call<VoucherResponse>,
                response: Response<VoucherResponse>
            ) {
//                val isiVoucher = response.body()!!.data
                val isiVoucher = response.body()!!.data
                val listHeroes = listOf(isiVoucher)
                if(response.isSuccessful){
                    val adapterLinear = VoucherAdapter(isiVoucher.toMutableList()){ Unit ->
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
                        }
                    rvM.apply {
                        //            layoutManager = GridLayoutManager(this@MainActivity, 3)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = adapterLinear
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




