package com.habibfr.mystoryapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.remote.response.DetailStoryResponse
import com.habibfr.mystoryapp.data.remote.response.ErrorResponse
import com.habibfr.mystoryapp.data.remote.response.ListStoryItem
import com.habibfr.mystoryapp.data.remote.response.Story
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    val detailStory: LiveData<Result<DetailStoryResponse>> = userRepository.detailStory

    fun getStoryById(id: String) {
        viewModelScope.launch {
            userRepository.getStoryById(id)
        }
    }
}