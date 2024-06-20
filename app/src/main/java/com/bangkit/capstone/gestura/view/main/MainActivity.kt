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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.databinding.ActivityMainBinding
import com.bangkit.capstone.gestura.view.ViewModelFactory
import com.bangkit.capstone.gestura.view.detectmovement.CameraActivity
import com.bangkit.capstone.gestura.view.login.LoginActivity
import com.bangkit.capstone.gestura.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    } //testing onlu
    private lateinit var mainViewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

//        viewModel.getSession().observe(this) { user ->
//            if (!user.isLogin) {
//                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
//                finish()
//            } else {
//                Log.d("maininfo", "Username: ${user.email}, Profile Picture URL: ${user.profile_picture_url}")
////                binding.tvUsername.text = user.email  //cek user model
////                binding.tvSelamat.text = greetingmessage()
//                Glide.with(this)
//                    .load(user.profile_picture_url)
//                    .error(R.drawable.ic_placeholder)
//                    .into(binding.ivtest)
//                viewModel.fetchUserProfile(user.token)
//            }
//        }

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val token = "your_token_here"
        mainViewModel.fetchUserProfile(token)


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

<<<<<<< HEAD
    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
        binding.cameraButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(100)
        val camera = ObjectAnimator.ofFloat(binding.cameraButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(name, message, logout)
            startDelay = 100
        }.start()
    }
=======
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

>>>>>>> origin/main
}