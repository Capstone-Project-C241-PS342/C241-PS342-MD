package com.bangkit.capstone.gestura.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.databinding.ActivityLoginBinding
import com.bangkit.capstone.gestura.databinding.CustomDialogBinding
import com.bangkit.capstone.gestura.view.ViewModelFactory
import com.bangkit.capstone.gestura.view.main.MainActivity
import com.bangkit.capstone.gestura.view.signup.SignupActivity
import com.dicoding.picodiploma.gestura.data.pref.UserModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

    private fun setupAction() {
        updateLoginButtonState()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                updateLoginButtonState()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    binding.passwordHelper.visibility = View.VISIBLE
                } else {
                    binding.passwordHelper.visibility = View.GONE
                }
                updateLoginButtonState()
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
        binding.passwordHelper.addTextChangedListener(textWatcher)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            binding.loading.visibility = View.VISIBLE
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                showAlert("Error", "Pastikan anda mengisi username dan password")
            }
        }

        binding.textbtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateLoginButtonState() {
        val username = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val isFormFilled = username.isNotEmpty() && password.isNotEmpty()
        binding.loginButton.isEnabled = isFormFilled
    }

    private fun login(email : String, password: String) {
        lifecycleScope.launch {
            val result = viewModel.login(email, password)
            result.onSuccess { user ->
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }.onFailure { exception ->
                val errorMessage = exception.message ?: "Error"
                Log.e("LoginActivity", "Login gagal: $errorMessage")
                showAlert("Email atau Password Salah", "Cek lagi data yang kamu masukin")
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)


        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()


        dialogBinding.successDialog.text = "Gabisa masuk nih"
        dialogBinding.successDialogDesc.text = "Coba cek email sama password kamu"
        dialogBinding.dialogLogin.text = "Oke"

        dialogBinding.dialogLogin.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
//        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val textbutton = ObjectAnimator.ofFloat(binding.textbtn, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
//                message,
                emailEditText,
                passwordEditText,
                textbutton,
                login
            )
            startDelay = 100
        }.start()
    }
}
