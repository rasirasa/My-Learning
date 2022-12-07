package com.ssrdi.co.id.myradboox.fragmentreseller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.adapter.SectionsPagerAdapter
import com.ssrdi.co.id.myradboox.databinding.FragmentDetailVoucherBinding
import com.ssrdi.co.id.myradboox.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_history, container, false)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionsPagerAdapter = SectionsPagerAdapter(this@HistoryFragment)
        val viewPager:ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs :TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        (activity as AppCompatActivity).supportActionBar?.elevation = 0f
        //supportActionBar?.elevation = 0f
    }
}