package com.example.scanpiramyds.database

import androidx.lifecycle.LiveData

class PiramydsRepository(private val piramydDao: PiramydDao) {

    val allPiramyds: LiveData<List<Piramyd>> = piramydDao.getAll()

    suspend fun insert(piramyd: Piramyd){
        piramydDao.insert(piramyd);
    }

    fun updateAll(piramyds: List<Piramyd>){
        piramydDao.updateAll(piramyds)
    }
}