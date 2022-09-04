package com.example.mmpplayer.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Media(
    val id: String,
    val title: String,
    val path: String,
    val duration: Long = 0,
    val folderName: String,
    val size: Long,
    val uri: Uri,
    val dateAdded: Long,
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readParcelable(Uri::class.java.classLoader)!!,
        parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(path)
        parcel.writeLong(duration)
        parcel.writeString(folderName)
        parcel.writeLong(size)
        parcel.writeParcelable(uri, flags)
        parcel.writeLong(dateAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}
