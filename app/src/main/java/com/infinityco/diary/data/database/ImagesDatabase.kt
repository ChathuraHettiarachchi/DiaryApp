package com.infinityco.diary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinityco.diary.data.database.entity.ImageToDelete
import com.infinityco.diary.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 2,
    exportSchema = false
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImageToDeleteDao
}