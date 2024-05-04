package com.habibfr.mystoryapp.data.remote.retrofit

import com.habibfr.mystoryapp.data.remote.response.DetailStoryResponse
import com.habibfr.mystoryapp.data.remote.response.FileUploadResponse
import com.habibfr.mystoryapp.data.remote.response.ListStoryItem
import com.habibfr.mystoryapp.data.remote.response.LoginResponse
import com.habibfr.mystoryapp.data.remote.response.RegisterResponse
import com.habibfr.mystoryapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse

//    @Multipart
//    @POST("stories/guest")
//    suspend fun uploadImage(
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//    ): FileUploadResponse
}