package com.ssrdi.co.id.myradboox.fragmentreseller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.api.model.ResellerOptionsData
import com.ssrdi.co.id.myradboox.api.model.ResellerResponse
import com.ssrdi.co.id.myradboox.databinding.FragmentGenerateBinding
import com.ssrdi.co.id.myradboox.model.GenerateResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class GenerateFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentGenerateBinding

    private lateinit var tokenLogin: String
    private lateinit var api: RadbooxApi

    private var nasSelected = 0
    private var profileSelected = 0
    private var serverSelected = 0
    private var userCharSelected = 0
    private var userModelSelected = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_generate, container, false)
        binding = FragmentGenerateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin
        api = RetrofitClient.getInstance(requireContext())

        getOptionsData()

        binding.inputRouterNas.onItemSelectedListener = this
        binding.inputUserCharacter.onItemSelectedListener = this
        binding.inputUserModel.onItemSelectedListener = this

        // user model
        setupContentDropdown(
            resources.getStringArray(R.array.list_username),
            binding.inputUserModel
        )

        setupContentDropdown(
            resources.getStringArray(R.array.list_user_character),
            binding.inputUserCharacter
        )

        setupContentDropdown(
            resources.getStringArray(R.array.list_username_length),
            binding.inputUserLength
        )

        binding.btnGenerate.setOnClickListener {
            // ambil jumlah voucher
            var jumlahVoucher = binding.inputNumberOfUser.text.toString().toInt()
            if (jumlahVoucher > 1_000) {
                jumlahVoucher = 1_000
            }

            var profile = binding.inputUserAssignProfile.text.toString()
            var userNameLength = binding.inputUserLength.text.toString()
            var userPrefix = binding.inputUsernamePrefix.text.toString()

            generateVoucher(
                jumlahVoucher,
                nas = "",
                server = "",
                profile = profile,
                usernameLength = userNameLength.toInt(),
                userCharacter = userCharSelected,
                usernamePrefix = userPrefix,
                userModel = userModelSelected
            )
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        if (view == binding.inputRouterNas) {
            Log.d("debug", "nas selected -> $pos")
            nasSelected = pos
        } else if (view == binding.inputUserAssignProfile) {
            Log.d("debug", "profile selected -> $pos")
            profileSelected = pos
        } else if (view == binding.inputHotspotServer) {
            Log.d("debug", "server selected -> $pos")
            serverSelected = pos
        } else if (view == binding.inputUserCharacter) {
            Log.d("debug", "user character selected -> $pos")
            userCharSelected = pos
        } else if (view == binding.inputUserModel) {
            Log.d("debug", "user model selected -> $pos")
            userModelSelected = pos
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun generateVoucher(
        jumlahVoucher: Int,
        profile: String,
        nas: String = "",
        server: String = "",
        usernameLength: Int,
        userCharacter: Int,
        userModel: Int,
        usernamePrefix: String = "voucher"
    ) {

        showLoading(true)

        api.generateResellerVoucher(
            authorization = "Bearer $tokenLogin",
            jumlah = jumlahVoucher,
            model = userModel,
            character = userCharacter,
            length = usernameLength,
            prefix = usernamePrefix,
            profile = profile,
            nas = nas,
            server = server,
            time = getCurrentTime()
        ).enqueue(object : Callback<GenerateResponse> {
            override fun onResponse(
                call: Call<GenerateResponse>,
                response: Response<GenerateResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Sukses Create Voucher", Toast.LENGTH_SHORT)
                        .show()

                    val navHostFragment =
                        parentFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    val navController = navHostFragment.navController

                    navController.popBackStack()

                } else {
                    showError("error data reseller null")
                }
            }

            override fun onFailure(call: Call<GenerateResponse>, t: Throwable) {
                Log.e("error", "error ->${t.localizedMessage}")
                showLoading(false)
            }

        })

    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val currentTime = sdf.format(Date())
        Log.d("debug", "current time $currentTime")
        return currentTime
    }

    private fun getOptionsData() {
        showLoading(true)

        api.getResellerOptions("Bearer $tokenLogin").enqueue(object : Callback<ResellerResponse> {
            override fun onResponse(
                call: Call<ResellerResponse>,
                response: Response<ResellerResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    Log.d("debug", "response -> ${response}")
                    val dataResellerOpt = response.body()?.data
                    if (dataResellerOpt != null) {
                        val nas = dataResellerOpt.nas
                        val profile = dataResellerOpt.profile
                        val server = dataResellerOpt.server

                        setupCustomContent(nas, binding.inputRouterNas)
                        setupCustomContent(profile, binding.inputUserAssignProfile)
                        setupCustomContent(server, binding.inputHotspotServer)

                    } else {
                        showError("error data reseller null")
                    }


                } else {
                    showError("error failed to get response options data")
                }
            }

            override fun onFailure(call: Call<ResellerResponse>, t: Throwable) {
                showLoading(false)
                showError("error ${t.localizedMessage}")
            }

        })
    }

    private fun setupCustomContent(
        data: List<ResellerOptionsData>,
        view: AutoCompleteTextView
    ) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, data)

        view.setAdapter(adapter)
        view.setText(adapter.getItem(0)!!.name, false)

    }

    private fun showError(message: String) {
        Log.e("error", "error -> $message")
    }

    private fun setupContentDropdown(
        @ArrayRes data: Array<out String>,
        autoCompleteView: AutoCompleteTextView
    ) {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        autoCompleteView.setAdapter(adapter)
        autoCompleteView.setText(adapter.getItem(0), false)
    }

    private fun showLoading(show: Boolean) {
        binding.loading.visibility = if (show) View.VISIBLE else View.GONE
    }
}