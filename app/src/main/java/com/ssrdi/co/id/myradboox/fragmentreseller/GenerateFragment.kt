package com.ssrdi.co.id.myradboox.fragmentreseller

import android.os.Bundle
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
import com.ssrdi.co.id.myradboox.databinding.FragmentDetailVoucherBinding
import com.ssrdi.co.id.myradboox.databinding.FragmentGenerateBinding

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
            Toast.makeText(requireContext(), "Btn Generate Click", Toast.LENGTH_SHORT).show()
        }
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