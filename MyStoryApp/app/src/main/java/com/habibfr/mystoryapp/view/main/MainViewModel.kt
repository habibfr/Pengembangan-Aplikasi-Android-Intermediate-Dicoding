package com.habibfr.mystoryapp.view.main

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.data.remote.response.ListStoryItem
import com.habibfr.mystoryapp.data.remote.response.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    val story: LiveData<Result<List<ListStoryItem>>> = repository.story

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStory() {
        viewModelScope.launch {
            repository.getStory()
        }
    }
}