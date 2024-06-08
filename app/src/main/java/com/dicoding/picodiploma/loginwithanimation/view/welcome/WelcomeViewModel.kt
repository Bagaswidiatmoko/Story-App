package com.dicoding.picodiploma.loginwithanimation.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.repository.UserRepository

class WelcomeViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getSession(): LiveData<String> {
        return userRepository.getSession().asLiveData()
    }
}