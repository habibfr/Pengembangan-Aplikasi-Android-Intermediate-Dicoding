package com.habibfr.mystoryapp.view.posting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.remote.response.FileUploadResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostingViewModel(private val repository: UserRepository) : ViewModel() {
    val postStatus: LiveData<Result<FileUploadResponse>> = repository.postStatus

    fun postStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            repository.postStory(file, description)
        }
    }
}