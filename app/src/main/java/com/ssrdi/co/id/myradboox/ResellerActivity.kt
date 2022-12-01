package com.ssrdi.co.id.myradboox

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.ActivityResellerBinding
import com.ssrdi.co.id.myradboox.fragmentreseller.GenerateFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HistoryFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HomeFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.SessionFragment
import com.ssrdi.co.id.myradboox.model.DetailResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResellerActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityResellerBinding
    private lateinit var mToggle: ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    private lateinit var preference : SharedPrefManager
    lateinit var retro: RadbooxApi
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        retro = RetrofitClient.getInstance(this)
        super.onCreate(savedInstanceState)

//        binding = ActivityResellerBinding.inflate(layoutInflater)
//       setContentView(binding.root)

        setContentView(R.layout.activity_reseller)



        drawerLayout = findViewById(R.id.drawerLayout)

        val navView: NavigationView = findViewById(R.id.nav_view)

//         memunculkan tombol burger menu
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // untuk toggle open dan close navigation
        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        // tambahkan mToggle ke drawer_layout sebagai pengendali open dan close drawer
        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

        cekTokenAktif()

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment(),it.title.toString())
                R.id.nav_session-> replaceFragment(SessionFragment(),it.title.toString())
                R.id.nav_history -> replaceFragment(HistoryFragment(),it.title.toString())
                R.id.nav_logout -> prosesLogout()
                //R.id.nav_logout -> Toast.makeText(applicationContext, "Clicked Logout", Toast.LENGTH_SHORT).show()

            }
            true
        }

    }
    //Set ke Fragment    }
    private fun replaceFragment(fragment: Fragment, title:String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }
    private fun prosesLogout(){
        var preference = SharedPrefManager.getInstance(applicationContext)
        preference.clearAll()
        val intent = Intent(this@ResellerActivity, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mn_generate -> {
                replaceFragment(GenerateFragment(), "Generate Voucher")
                true
            }
            else -> {
                mToggle.onOptionsItemSelected(item)
            }
        }




        return mToggle.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.generate, menu)
        return true
    }



    private fun cekTokenAktif(){
        var tokenLogin = SharedPrefManager.getInstance(this).tokenLogin
        retro.detailAdmin("Bearer $tokenLogin").enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if(response.isSuccessful){
                    var role = SharedPrefManager.getInstance(this@ResellerActivity).role
                    //checkUserRole(this@ResellerActivity, role)
                } else if(response.code() == 406){
                    prosesLogout()
                }else if(response.code() ==402){
                    val intent=Intent(this@ResellerActivity, ExpiredActivity::class.java)
                    startActivity(intent)
                }
            }


            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })

    }

}