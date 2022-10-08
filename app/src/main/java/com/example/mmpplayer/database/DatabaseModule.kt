package com.example.mmpplayer.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): MusicDatabase {
        return Room.databaseBuilder(context, MusicDatabase::class.java, "music_database").build()
    }

    @Singleton
    @Provides
    fun provideMusicDao(db: MusicDatabase): MusicDao {
        return db.getMusicDao()
    }
}