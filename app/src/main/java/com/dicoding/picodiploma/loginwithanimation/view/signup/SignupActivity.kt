package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.AuthViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val signUpViewModel: SignupViewModel by viewModels {
        AuthViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 2f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 2f).setDuration(300)
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 2f).setDuration(300)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 2f).setDuration(300)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 2f).setDuration(300)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 2f).setDuration(300)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 2f).setDuration(300)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 2f).setDuration(300)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 2f).setDuration(300)
        AnimatorSet().apply {
            playSequentially(image, title, nameTextView, nameEditTextLayout, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, signup)
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
            signupButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                signUpViewModel.register(name, email, password).observe(this@SignupActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                progressbar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                progressbar.visibility = View.GONE
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle(getString(R.string.yeah))
                                    setMessage(getString(R.string.akun_sudah_jadi_nih_yuk_login_ke_story_app))
                                    setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                progressbar.visibility = View.GONE
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle(getString(R.string.gagal_membuat_akun))
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