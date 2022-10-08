package com.example.mmpplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.Playlist

@Database(entities = [Media::class,Playlist::class], version = 1)
@TypeConverters(Converters::class)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun getMusicDao() : MusicDao
}