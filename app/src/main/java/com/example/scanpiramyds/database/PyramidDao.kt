package com.example.scanpiramyds.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PyramidDao {
    @Query("SELECT * FROM pyramid")
    fun getAll(): LiveData<List<Pyramid>>

    @Query("SELECT * FROM pyramid WHERE code IN(:piramydCodes)")
    fun loadAllByCodes(piramydCodes: IntArray) : LiveData<List<Pyramid>>

    @Query("SELECT * FROM pyramid WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Pyramid

    @Update
    fun updateAll (pyramids: List<Pyramid>)

    @Update
    fun update(pyramid: Pyramid)

    @Insert
    fun insertAll(vararg pyramids: Pyramid)

    @Insert
    fun insert(pyramid: Pyramid)

    @Delete
    fun delete(pyramid: Pyramid)
}