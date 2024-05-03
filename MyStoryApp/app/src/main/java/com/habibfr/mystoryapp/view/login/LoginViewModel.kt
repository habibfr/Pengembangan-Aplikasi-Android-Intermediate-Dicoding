package com.habibfr.mystoryapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.data.remote.response.LoginResponse
import com.habibfr.mystoryapp.data.remote.response.LoginResult
import com.habibfr.mystoryapp.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    val dataLogin: LiveData<Result<LoginResult>> = userRepository.dataUser

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password)
        }
    }
}