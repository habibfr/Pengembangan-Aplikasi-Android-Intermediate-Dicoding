package com.habibfr.mystoryapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch
import com.habibfr.mystoryapp.data.Result

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    val registerStatus: LiveData<Result<RegisterResponse>> = userRepository.registerStatus

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            userRepository.register(username, email, password)
        }
    }
}