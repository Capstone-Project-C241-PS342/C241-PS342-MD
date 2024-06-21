package com.bangkit.capstone.gestura.view.main

import ProfileFragment
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.databinding.ActivityMainBinding
import com.bangkit.capstone.gestura.view.ViewModelFactory
import com.bangkit.capstone.gestura.view.detectmovement.CameraActivity
import com.bangkit.capstone.gestura.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    } //testing onlu




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())



        binding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                else -> {
                }
            }
            true
        }
        setupView()

        // Find the CardView and set an OnClickListener
        val btnScan: CardView = findViewById(R.id.btnscan)
        btnScan.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        // Implementasi contoh, periksa apakah token atau sesi pengguna masih aktif
        val token = retrieveUserToken() // Metode untuk mengambil token dari penyimpanan
        return token.isNotEmpty() // Misalnya, periksa apakah token tidak kosong
    }

    private fun retrieveUserToken(): String {
        // Implementasi contoh, ambil token dari penyimpanan yang digunakan
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_token", "") ?: ""
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}