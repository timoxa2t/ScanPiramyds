package com.example.scanpiramyds.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Piramyd(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "checked") var checked: Boolean
)

