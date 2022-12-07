package com.ssrdi.co.id.myradboox.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssrdi.co.id.myradboox.fragmentreseller.HistoryBalanceFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HistoryFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HistoryGenerateFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HistoryTransactionFragment

class SectionsPagerAdapter(activity: HistoryFragment) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 -> fragment = HistoryBalanceFragment()
            1 -> fragment = HistoryGenerateFragment()
            2 -> fragment = HistoryTransactionFragment()
        }
        return fragment as Fragment
    }

}