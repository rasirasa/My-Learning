package com.ssrdi.co.id.myradboox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_reseller.*

class ResellerActivity : AppCompatActivity() {

    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reseller)

        // memunculkan tombol burger menu
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        // untuk toggle open dan close navigation
//        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
//        // tambahkan mToggle ke drawer_layout sebagai pengendali open dan close drawer
//        drawerLayout.addDrawerListener(mToggle)
//        mToggle.syncState()

    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return mToggle.onOptionsItemSelected(item)
    }

}