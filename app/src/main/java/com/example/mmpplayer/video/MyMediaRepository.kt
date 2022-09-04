package com.example.mmpplayer.video

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mmpplayer.model.Folder
import com.example.mmpplayer.model.Media
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MyMediaRepository(private val source: MyMediaDataSource, private val dispatcher:CoroutineDispatcher) {
    suspend fun getAllVideos():ArrayList<Media>{
        return withContext(dispatcher){
            source.getAllVideos()
        }
    }
    suspend fun getAllVideoFolders():ArrayList<Folder>{
        return withContext(dispatcher){
            source.getAllVideoFolders()
        }
    }
     suspend fun getSpecificFolderVideos(folderId:String):ArrayList<Media>{
        return withContext(dispatcher){
            source.getAllFolderVideos(folderId)
        }
    }

 @RequiresApi(Build.VERSION_CODES.Q)
 suspend fun getAllMusic():ArrayList<Media>{
        return withContext(dispatcher){
            source.getAllMusic()
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun getAllMusicFolders():ArrayList<Folder>{
        return withContext(dispatcher){
            source.getAllMusicFolders()
        }
    }
     @RequiresApi(Build.VERSION_CODES.Q)
     suspend fun getSpecificFolderMusic(folderId:String):ArrayList<Media>{
        return withContext(dispatcher){
            source.getAllFolderMusic(folderId)
        }
    }


}