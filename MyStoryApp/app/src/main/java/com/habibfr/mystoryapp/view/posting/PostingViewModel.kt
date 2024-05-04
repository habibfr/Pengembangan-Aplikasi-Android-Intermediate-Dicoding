package com.habibfr.mystoryapp.view.posting

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.data.remote.response.FileUploadResponse
import com.habibfr.mystoryapp.data.remote.response.ListStoryItem
import com.habibfr.mystoryapp.data.remote.response.LoginResult
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