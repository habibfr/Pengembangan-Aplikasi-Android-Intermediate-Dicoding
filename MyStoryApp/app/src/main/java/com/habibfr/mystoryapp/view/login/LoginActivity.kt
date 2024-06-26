package com.habibfr.mystoryapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.habibfr.mystoryapp.R
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.databinding.ActivityLoginBinding
import com.habibfr.mystoryapp.view.ViewModelFactory
import com.habibfr.mystoryapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
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
        setupInput()
    }

    private fun setupInput() {
        setMyButtonEnable()

        binding.passwordEditText.setTextInputLayout(binding.passwordEditTextLayout)
        binding.passwordEditText.setButton(binding.loginButton)

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (!s.toString().trim().matches(Regex(emailPattern))) {
                    binding.emailEditTextLayout.error = getString(R.string.error_email)
                } else {
                    binding.emailEditTextLayout.error = null
                }

                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        setMyButtonEnable()
    }

    private fun setMyButtonEnable() {
        val isEmailValid = binding.emailEditTextLayout.error == null
        val isPasswordValid = binding.passwordEditText.getButtonIsValid()
        binding.loginButton.isEnabled = isEmailValid && isPasswordValid
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
        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            loginViewModel.login(email, password)

            loginViewModel.dataLogin.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE

                            result.data.token?.let { it1 ->
                                UserModel(
                                    email,
                                    it1
                                )
                            }?.let { it2 -> loginViewModel.saveSession(it2) }

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this, result.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}