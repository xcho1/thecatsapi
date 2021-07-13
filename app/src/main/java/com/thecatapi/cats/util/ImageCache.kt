package com.thecatapi.cats.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.thecatapi.cats.BuildConfig
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageCache {

    private const val COMPRESS_QUALITY = 100

    fun createTempFile(context: Context): File = File.createTempFile("tmp_image_file",".jpg", context.cacheDir)
        .apply {
            createNewFile()
            deleteOnExit()
        }

    fun saveImgToCache(context: Context, cachePath: File, uri: Uri): File {
        try {
            val stream = FileOutputStream("$cachePath")
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, stream)
            stream.close()
        } catch (e: IOException) {
            Timber.e(e, "saveImgToCache error")
        }
        return cachePath
    }

    fun toUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context.applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            file
        )
    }
}