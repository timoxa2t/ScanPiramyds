package com.example.scanpiramyds.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PiramydViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PiramydsRepository

    var allPiramyds: LiveData<List<Piramyd>>


    init {
        val piramydDao = AppDatabase.getDatabase(application, viewModelScope).piramydDao()
        repository = PiramydsRepository(piramydDao)
        allPiramyds = repository.allPiramyds


    }

    fun insert(piramyd: Piramyd) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(piramyd)
    }

}