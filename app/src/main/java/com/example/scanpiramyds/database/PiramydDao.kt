package com.example.scanpiramyds.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PiramydDao {
    @Query("SELECT * FROM piramyd")
    fun getAll(): LiveData<List<Piramyd>>

    @Query("SELECT * FROM piramyd WHERE code IN(:piramydCodes)")
    fun loadAllByCodes(piramydCodes: IntArray) : LiveData<List<Piramyd>>

    @Query("SELECT * FROM piramyd WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Piramyd

    @Insert
    fun insertAll(vararg piramyds: Piramyd)
    @Insert
    fun insert(piramyd: Piramyd)

    @Delete
    fun delete(piramyd: Piramyd)
}