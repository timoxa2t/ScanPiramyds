package com.example.scanpiramyds.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(application: Application): ViewModelProvider.Factory{
    private var mApplication: Application

    init {
        mApplication = application
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass === PiramydViewModel::class.java){
            return PiramydViewModel(mApplication) as T
        }
        return null as T
    }
}