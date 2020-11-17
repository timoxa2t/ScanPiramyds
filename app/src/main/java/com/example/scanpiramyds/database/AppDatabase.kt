package com.example.scanpiramyds.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Pyramid::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun piramydDao(): PyramidDao

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

        suspend fun populateDatabase(pyramidDao: PyramidDao){
//            var piramydList = mutableListOf<Piramyd>()
//          piramydList.add(Piramyd("000001049", "1066", false))
//          piramydList.add(Piramyd("000002750", "1417", false))
//          piramydList.add(Piramyd("000002802", "1469", false))
//
//
//            piramydList.forEach({
//                piramydDao.delete(it)
//                piramydDao.insert(it)
//            }
//            )

        }
    }

}