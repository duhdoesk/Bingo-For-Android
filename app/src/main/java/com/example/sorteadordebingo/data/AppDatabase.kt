package com.example.sorteadordebingo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.model.Session
import com.example.sorteadordebingo.model.Theme

@Database(
    entities = [
        Session::class,
        Theme::class,
        Element::class
    ],
    exportSchema = true,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun elementDao(): ElementDao
    abstract fun themeDao(): ThemeDao
    abstract fun sessionDao(): SessionDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDatabase"
                )
                    .createFromAsset("data.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}