package com.habibfr.mystoryapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.entity.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    val storyLocation: LiveData<Result<List<ListStoryItem>>> = repository.storyLocation

    init {
        getStoryWithLocation()
    }

    fun getStoryWithLocation() {
        viewModelScope.launch {
            repository.getStoryWithLocation()
        }
    }
}