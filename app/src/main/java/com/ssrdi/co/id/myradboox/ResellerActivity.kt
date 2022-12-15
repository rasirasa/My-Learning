package com.ssrdi.co.id.myradboox

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.ActivityResellerBinding
import com.ssrdi.co.id.myradboox.fragmentreseller.GenerateFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HistoryFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.HomeFragment
import com.ssrdi.co.id.myradboox.fragmentreseller.SessionFragment
import com.ssrdi.co.id.myradboox.model.DetailResponse
import com.ssrdi.co.id.myradboox.model.StockResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResellerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityResellerBinding
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var preference: SharedPrefManager

    lateinit var retro: RadbooxApi

    private var tokenLogin: String = ""


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reseller)

        retro = RetrofitClient.getInstance(this)
        tokenLogin = SharedPrefManager.getInstance(this).tokenLogin


        setupNavigationDrawer()

        cekTokenAktif()

    }

    private fun setupNavigationDrawer() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)

        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)

//         memunculkan tombol burger menu
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // untuk toggle open dan close navigation
        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        // tambahkan mToggle ke drawer_layout sebagai pengendali open dan close drawer
        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.homeFragment)
            }
            R.id.nav_session -> {
                navController.navigate(R.id.sessionFragment)
            }
            R.id.nav_history -> {
                navController.navigate(R.id.historyFragment)
            }
            R.id.nav_share -> {
                shareApp()
            }
            R.id.nav_logout -> {
                prosesLogout()
            }
        }
        return true
    }

    private fun prosesLogout() {
        var preference = SharedPrefManager.getInstance(applicationContext)
        preference.clearAll()
        val intent = Intent(this@ResellerActivity, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mn_generate -> {
                navController.navigate(R.id.action_home_to_generateVoucherFragment)
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


    private fun cekTokenAktif() {

        retro.detailAdmin("Bearer $tokenLogin").enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    var role = SharedPrefManager.getInstance(this@ResellerActivity).role

                    //Jika Api Sudah Dapat Ke Url Image


//                    var pic :String? = response.body()?.address
//                    var uname :String? = response.body()?.username
//                    val navigationView : NavigationView = findViewById<NavigationView>(R.id.nav_view);
//                    val headerView : View = LayoutInflater.from(this@ResellerActivity).inflate(R.layout.nav_header, navigationView, false);
//                    navigationView.addHeaderView(headerView);
//
//                    val iv :ImageView = headerView.findViewById<ImageView>(R.id.profile_pic)
//                    val username:EditText = headerView.findViewById<EditText>(R.id.user_name_profile)
//                    username.setText("$uname")
//
//                    Glide.with(this@ResellerActivity)
//                    .load(pic)
//                    .placeholder(R.drawable.ic_launcher_background)
//                        .centerCrop()
//                    .into(iv)
//                Picasso.get().load(hero.image).into(imgHeroes)

                    //checkUserRole(this@ResellerActivity, role)
                } else if (response.code() == 406) {
                    prosesLogout()
                } else if (response.code() == 402) {
                    val intent = Intent(this@ResellerActivity, ExpiredActivity::class.java)
                    startActivity(intent)
                }
            }


            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })

    }

    private fun shareApp() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Application Name")
        intent.type = "text/plain"
        var shareMessage: String = "\nLet Me Recommend You This Application\n\n"
        shareMessage =
            shareMessage + "https://play.google.com/store/apps/details?id=com.ssr.radboox" + BuildConfig.APPLICATION_ID + "\n\n"
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(intent, "Share To :"))

    }

}