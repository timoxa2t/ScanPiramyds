package com.example.scanpiramyds.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Appendable

@Database(entities = arrayOf(Piramyd::class), version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun piramydDao(): PiramydDao

    companion object {

        private var INSTANCE: AppDatabase?  = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "database-name")
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch(Dispatchers.IO) { populateDatabase(database.piramydDao()) } }
        }

        suspend fun populateDatabase(piramydDao: PiramydDao){
//            piramydDao.insert(Piramyd("000002487", "1154"))
//            piramydDao.insert(Piramyd("000002488", "1155"))
//            piramydDao.insert(Piramyd("000002489", "1156"))
//            piramydDao.insert(Piramyd("000002490", "1157"))
//            piramydDao.insert(Piramyd("000002491", "1158"))
        }
    }

}