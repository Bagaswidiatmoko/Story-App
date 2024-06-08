package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.AuthViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 2f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 2f).setDuration(300)
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 2f).setDuration(300)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 2f).setDuration(300)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 2f).setDuration(300)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 2f).setDuration(300)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 2f).setDuration(300)
        AnimatorSet().apply {
            playSequentially(image, title, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login)
            start()
        }
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
        with(binding) {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                loginViewModel.login(email, password).observe(this@LoginActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                progressbar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                progressbar.visibility = View.GONE
                                val loginResponse = it.data.loginResult
                                if (loginResponse != null) {
                                    loginResponse.token?.let { it1 ->
                                        loginViewModel.saveTokenSession(
                                            it1
                                        )
                                    }
                                }

                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setTitle(getString(R.string.yeah))
                                    setMessage(getString(R.string.anda_berhasil_login))
                                    setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                progressbar.visibility = View.GONE
                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setTitle(getString(R.string.gagal_login))
                                    setMessage(it.error)
                                    setPositiveButton(getString(R.string.ok)) { _, _ ->
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}

