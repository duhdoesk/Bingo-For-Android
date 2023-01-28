package com.example.sorteadordebingo.di

import android.content.Context
import androidx.room.Room
import com.example.sorteadordebingo.data.AppDatabase
import com.example.sorteadordebingo.data.ElementDao
import com.example.sorteadordebingo.data.ThemeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appDatabase"
        )
            .createFromAsset("data.db")
            .build()

    @Singleton
    @Provides
    fun provideElementDao(appDatabase: AppDatabase) : ElementDao {
        return appDatabase.elementDao()
    }

    @Singleton
    @Provides
    fun provideThemeDao(appDatabase: AppDatabase) : ThemeDao {
        return appDatabase.themeDao()
    }
}