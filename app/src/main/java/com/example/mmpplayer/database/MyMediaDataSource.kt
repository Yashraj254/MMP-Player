package com.example.mmpplayer.database

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.mmpplayer.model.Folder
import com.example.mmpplayer.model.Media
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

class MyMediaDataSource @Inject constructor(private val contentResolver: ContentResolver) {


    private val videosList = ArrayList<Media>()
    private val folderList = ArrayList<Folder>()
    private val musicList = ArrayList<Media>()
    fun getAllVideos(): ArrayList<Media> {
//        val videosList: ArrayList<Media> = ArrayList()
        val projection = arrayOf(
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
        )
//        videosList.clear()

        val cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null)
        while (cursor!!.moveToNext()) {
            if (cursor.getString(2) != null && cursor.getString(4) != null) {
                val file = File(cursor.getString(2))
                val uri = Uri.fromFile(file)
                val videoData = Media(0,cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getString(4),
                    cursor.getLong(5),
                    uri.toString(),
                    cursor.getLong(6)
                )
                if (File(videoData.path).exists())
                    videosList.add(videoData)
            }
        }
        cursor.close()
        return videosList
    }

    fun getAllVideoFolders(): ArrayList<Folder> {
//        val folderList = ArrayList<Folder>()
        val tempFoldersList = ArrayList<String>()
        val projection = arrayOf(
            MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        )

        val cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null)
        while (cursor!!.moveToNext()) {
            if (cursor.getString(1) != null) {
                val folderData =
                    Folder(
                        cursor.getString(0),
                        cursor.getString(1),
                    )
                if (!tempFoldersList.contains(folderData.id)) {
                    tempFoldersList.add(folderData.id)
                    folderList.add(folderData)

                }
            }
        }
        cursor.close()
        return folderList
    }

    fun getAllFolderVideos(folderId: String): ArrayList<Media> {

//        val videosList = ArrayList<Media>()
        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
        val projection = arrayOf(
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED
        )

        val cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(folderId),
            null)
        while (cursor!!.moveToNext()) {
            if (cursor.getString(2) != null) {
                val file = File(cursor.getString(2))
                val uri = Uri.fromFile(file)
                val videoData = Media(0,cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getString(4),
                    cursor.getLong(5),
                    uri.toString(),
                    cursor.getLong(6)
                )
                if (File(videoData.path).exists())
                    videosList.add(videoData)
            }
        }
        cursor.close()
        return videosList

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAllMusic(): ArrayList<Media> {
//        val musicList: ArrayList<Media> = ArrayList()
        val projection = arrayOf(
            MediaStore.Audio.Media.BUCKET_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED,
        )
//        musicList.clear()

        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null)
        while (cursor!!.moveToNext()) {
            if (cursor.getString(2) != null && cursor.getString(4) != null) {
                val file = File(cursor.getString(2))
                val uri = Uri.fromFile(file)
                val musicData = Media(0,cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getString(4),
                    cursor.getLong(5),
                    uri.toString(),
                    cursor.getLong(6)
                )
                if (File(musicData.path).exists())
                    musicList.add(musicData)
            }
        }
        cursor.close()
        return musicList
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAllMusicFolders(): ArrayList<Folder> {
//        val folderList = ArrayList<Folder>()
        val tempFoldersList = ArrayList<String>()
        val projection = arrayOf(
            MediaStore.Audio.Media.BUCKET_ID, MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
        )

        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null)
        while (cursor!!.moveToNext()) {
            if (cursor.getString(1) != null) {
                val folderData =
                    Folder(
                        cursor.getString(0),
                        cursor.getString(1),
                    )
                if (!tempFoldersList.contains(folderData.id)) {
                    tempFoldersList.add(folderData.id)
                    folderList.add(folderData)

                }
            }
        }
        cursor.close()
        return folderList
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAllFolderMusic(folderId: String): ArrayList<Media> {

//        val musicList = ArrayList<Media>()
        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
        val projection = arrayOf(
            MediaStore.Audio.Media.BUCKET_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED
        )
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(folderId),
            null)
        while (cursor!!.moveToNext()) {
            if (cursor.getString(2) != null) {
                val file = File(cursor.getString(2))
                val uri = Uri.fromFile(file)
                val musicData = Media(0,cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getString(4),
                    cursor.getLong(5),
                    uri.toString(),
                    cursor.getLong(6)
                )
                if (File(musicData.path).exists())
                    musicList.add(musicData)
            }
        }
        cursor.close()
        return musicList

    }
}