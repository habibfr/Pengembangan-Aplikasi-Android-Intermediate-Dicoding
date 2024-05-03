package com.habibfr.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.habibfr.mystoryapp.data.pref.UserModel
import com.habibfr.mystoryapp.data.pref.UserPreference
import com.habibfr.mystoryapp.data.remote.response.DetailStoryResponse
import com.habibfr.mystoryapp.data.remote.response.ErrorResponse
import com.habibfr.mystoryapp.data.remote.response.ListStoryItem
import com.habibfr.mystoryapp.data.remote.response.LoginResult
import com.habibfr.mystoryapp.data.remote.response.RegisterResponse
import com.habibfr.mystoryapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {

    private val _registerStatus = MutableLiveData<Result<RegisterResponse>>()
    val registerStatus: LiveData<Result<RegisterResponse>> = _registerStatus

    private val _dataUser = MutableLiveData<Result<LoginResult>>()
    val dataUser: LiveData<Result<LoginResult>> = _dataUser

    private val _story = MutableLiveData<Result<List<ListStoryItem>>>()
    val story: LiveData<Result<List<ListStoryItem>>> = _story

    private val _detailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val detailStory: LiveData<Result<DetailStoryResponse>> = _detailStory

    suspend fun register(name: String, email: String, password: String) {
        _registerStatus.value = Result.Loading
        try {
            val response = apiService.register(name, email, password)
            _registerStatus.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _registerStatus.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun login(email: String, password: String) {
        _dataUser.value = Result.Loading
        try {
            val response = apiService.login(email, password)
            _dataUser.value = Result.Success(response.loginResult)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _dataUser.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    suspend fun getStory() {
        _story.value = Result.Loading
        try {
            val response = apiService.getStories()
            _story.value = Result.Success(response.listStory)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _story.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    suspend fun getStoryById(id: String) {
        _detailStory.value = Result.Loading
        try {
            val response = apiService.getStoryById(id)
            _detailStory.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _detailStory.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}