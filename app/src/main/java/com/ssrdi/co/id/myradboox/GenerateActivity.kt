package com.ssrdi.co.id.myradboox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.ArrayRes
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.api.model.ResellerResponse
import com.ssrdi.co.id.myradboox.databinding.ActivityGenerateBinding
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenerateBinding.inflate(layoutInflater)

        setContentView(binding.root)



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

        setupContentDropdown(
            resources.getStringArray(R.array.list_assign_profile),
            binding.inputUserAssignProfile
        )

        setupContentDropdown(
            resources.getStringArray(R.array.list_router_hotspot),
            binding.inputRouterNas
        )

        setupContentDropdown(
            resources.getStringArray(R.array.list_router_hotspot),
            binding.inputHotspotServer
        )

        binding.btnGenerate.setOnClickListener {
            Toast.makeText(this, "Btn Generate Click", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupContentDropdown(
        @ArrayRes data: Array<out String>,
        autoCompleteView: AutoCompleteTextView
    ) {
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, data)
        autoCompleteView.setAdapter(adapter)
        autoCompleteView.setText(adapter.getItem(0), false)
    }
}