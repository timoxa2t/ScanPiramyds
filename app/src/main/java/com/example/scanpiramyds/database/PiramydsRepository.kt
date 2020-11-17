package com.example.scanpiramyds.database

import androidx.lifecycle.LiveData

class PiramydsRepository(private val pyramidDao: PyramidDao) {

    val allPiramyds: LiveData<List<Pyramid>> = pyramidDao.getAll()

    suspend fun insert(pyramid: Pyramid){
        pyramidDao.insert(pyramid);
    }

    fun update (pyramid: Pyramid){
        pyramidDao.update(pyramid)
    }

    fun updateAll(pyramids: List<Pyramid>){
        pyramidDao.updateAll(pyramids)
    }
}