package com.dicoding.picodiploma.loginwithanimation.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun addStory(file: MultipartBody.Part, description: RequestBody) =
        repository.addStory(file, description)

    fun addStoryWithLocation(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody
    ) = repository.addStoryWithLocation(file, description, lat, lon)

}