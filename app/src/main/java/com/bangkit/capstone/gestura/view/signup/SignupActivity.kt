package com.bangkit.capstone.gestura.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.gestura.databinding.ActivitySignupBinding
import com.bangkit.capstone.gestura.view.ViewModelFactory
import kotlinx.coroutines.launch


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels { ViewModelFactory.getInstance(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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

//    private fun setupAction() {
//        binding.loginButton.setOnClickListener {
//            val username = binding.usernameEditText.text.toString()
//            val email = binding.emailEditText.text.toString()
//            val password = binding.passwordEditText.text.toString()
//
////            if (password.length < 8) {
////                binding.passwordEditTextLayout.error = "Password tidak boleh kurang dari 8 karakter"
////                return@setOnClickListener
////            }
//
//            lifecycleScope.launch {
//                try {
//                    val response = signupViewModel.register(username, email, password)
//                    if (response.error == false) {
//                        AlertDialog.Builder(this@SignupActivity).apply {
//                            setTitle("Yeah!")
//                            setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
//                            setPositiveButton("Lanjut") { _, _ -> finish() }
//                            create()
//                            show()
//                        }
//                    } else {
//                        Toast.makeText(this@SignupActivity, "Registration failed: ${response.message}", Toast.LENGTH_SHORT).show()
//                        Log.e("SignupActivity", "Registration error: ${response.message}")
//                    }
//                } catch (e: Exception) {
//                    Log.e("SignupActivity", "Registration error", e)
//                    Toast.makeText(this@SignupActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    // SignupActivity.kt
    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            lifecycleScope.launch {
                try {
                    val response = signupViewModel.register(username, email, password)
                    if (response == "User registered") {
                        AlertDialog.Builder(this@SignupActivity).apply {
                            setTitle("Yeah!")
                            setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                            setPositiveButton("Lanjut") { _, _ -> finish() }
                            create()
                            show()
                        }
                    } else if (response.contains("Username or email already exists")) {
                        Toast.makeText(this@SignupActivity, "Username atau email sudah digunakan", Toast.LENGTH_SHORT).show()
                        Log.e("SignupActivity", "Registration error: $response")
                    } else {
                        Toast.makeText(this@SignupActivity, response, Toast.LENGTH_SHORT).show()
                        Log.e("SignupActivity", "Unknown registration error: $response")
                    }
                } catch (e: Exception) {
                    Log.e("SignupActivity", "Registration error", e)
                    Toast.makeText(this@SignupActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val usernameTextView =
            ObjectAnimator.ofFloat(binding.usernameEditText, View.ALPHA, 1f).setDuration(100)
        val usernameEditTextLayout =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val description = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                usernameTextView,
                usernameEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                description,
            )
            startDelay = 100
        }.start()
    }
}