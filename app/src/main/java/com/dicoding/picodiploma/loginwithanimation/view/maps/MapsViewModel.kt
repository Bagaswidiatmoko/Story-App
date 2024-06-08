package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.repository.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getStoryWithLocation(): LiveData<Result<StoryResponse>> =
        repository.getListStoryWithLocation()

}