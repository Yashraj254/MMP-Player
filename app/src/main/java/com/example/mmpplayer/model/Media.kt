package com.example.mmpplayer.model

import android.net.Uri
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_music")
data class Media(
    @PrimaryKey(autoGenerate = true)
    val roomId:Long,
    val id: String,
    val title: String,
    val path: String,
    val duration: Long = 0,
    val folderName: String,
    val size: Long,
    val uri: String,
    val dateAdded: Long,
    var isSelected: Boolean = false,
) :
    Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readBoolean()) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(roomId)
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(path)
        parcel.writeLong(duration)
        parcel.writeString(folderName)
        parcel.writeLong(size)
        parcel.writeString(uri)
        parcel.writeLong(dateAdded)
        parcel.writeBoolean(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}
