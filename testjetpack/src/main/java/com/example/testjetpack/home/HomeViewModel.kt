package com.example.testjetpack.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var liveData = HomeLiveData();

    fun getHomeLiveData(): HomeLiveData {
        return liveData;
    }

}