package com.bangkit.capstone.gestura.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.databinding.ActivitySignupBinding
import com.bangkit.capstone.gestura.databinding.CustomDialogBinding
import com.bangkit.capstone.gestura.view.ViewModelFactory
import com.bangkit.capstone.gestura.view.login.LoginActivity
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
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                updateSignupButtonState()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    binding.passwordHelper.visibility = View.VISIBLE
                } else {
                    binding.passwordHelper.visibility = View.GONE
                }
                updateSignupButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        var isPasswordVisible = false

        binding.show.setOnClickListener {
            val typeface = binding.passwordEditText.typeface

            if (isPasswordVisible) {
                binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.show.setImageResource(R.drawable.show)
            } else {
                binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.show.setImageResource(R.drawable.hide)
            }

            binding.passwordEditText.typeface = typeface
            binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
            isPasswordVisible = !isPasswordVisible
        }

        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        binding.usernameEditText.addTextChangedListener(textWatcher)
        binding.passwordHelper.addTextChangedListener(textWatcher)

        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            binding.loading.visibility = View.VISIBLE

            lifecycleScope.launch {
                try {
                    val response = signupViewModel.register(username, email, password)
                    if (response == "User registered") {
                        showSuccessDialog()
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

        binding.textbtn.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSuccessDialog() {
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)


        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()


        dialogBinding.successDialog.text = "Kamu udah terdaftar nih"
        dialogBinding.successDialogDesc.text = "Yuk masuk, terus belajar"

        dialogBinding.dialogLogin.setOnClickListener {
            alertDialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialog.show()
    }

    private fun updateSignupButtonState() {
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val isFormFilled = email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()

        binding.signupButton.isEnabled = isFormFilled
    }



    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val usernameTextView =
            ObjectAnimator.ofFloat(binding.usernameEditText, View.ALPHA, 1f).setDuration(100)
        val usernameEditTextLayout =
            ObjectAnimator.ofFloat(binding.usernameEditText, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val textbutton = ObjectAnimator.ofFloat(binding.textbtn, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditText,
                usernameTextView,
                usernameEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                textbutton,
            )
            startDelay = 100
        }.start()
    }
}