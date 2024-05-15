package com.habibfr.mystoryapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.habibfr.mystoryapp.data.entity.ListStoryItem

data class StoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


