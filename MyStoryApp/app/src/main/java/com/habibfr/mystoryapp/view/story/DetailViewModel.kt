package com.habibfr.mystoryapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.remote.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    val detailStory: LiveData<Result<DetailStoryResponse>> = userRepository.detailStory

    fun getStoryById(id: String) {
        viewModelScope.launch {
            userRepository.getStoryById(id)
        }
    }
}