package com.example.scanpiramyds.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PiramydViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PiramydsRepository

    var allPyramids: LiveData<List<Pyramid>>


    init {
        val piramydDao = AppDatabase.getDatabase(application, viewModelScope).piramydDao()
        repository = PiramydsRepository(piramydDao)
        allPyramids = repository.allPiramyds


    }

    fun insert(pyramid: Pyramid) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(pyramid)
    }
    fun update(pyramid: Pyramid) = viewModelScope.launch (Dispatchers.IO){
        repository.update(pyramid)
    }

    fun updateAll() = viewModelScope.launch(Dispatchers.IO){
        val piramydsList = allPyramids.value
        if(piramydsList != null){
            repository.updateAll(piramydsList)
        }
    }

}