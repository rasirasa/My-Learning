package com.ssrdi.co.id.myradboox.fragmentreseller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.ArrayRes
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.api.model.ResellerOptionsData
import com.ssrdi.co.id.myradboox.api.model.ResellerResponse
import com.ssrdi.co.id.myradboox.databinding.FragmentDetailVoucherBinding
import com.ssrdi.co.id.myradboox.databinding.FragmentGenerateBinding
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenerateFragment : Fragment() {
    private lateinit var binding: FragmentGenerateBinding
    lateinit var retro: RadbooxApi

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

        getOptionsData()

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
            Toast.makeText(requireContext(), "Btn Generate Click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getOptionsData() {
        val api = RetrofitClient.getInstance(requireContext())
        val tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        api.getResellerOptions("Bearer $tokenLogin").enqueue(object : Callback<ResellerResponse> {
            override fun onResponse(
                call: Call<ResellerResponse>,
                response: Response<ResellerResponse>
            ) {
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
}