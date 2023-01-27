package ru.akimychev.convertjpgtopng.model.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import io.reactivex.rxjava3.core.Single
import ru.akimychev.convertjpgtopng.model.ImageConverterRepository
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageConverterRepositoryImpl(private val currentContext: Context) : ImageConverterRepository {
    override fun convertToPng(uri: Uri?): Single<File> {
        uri?.let {
            val externalStorageState = Environment.getExternalStorageState()
            if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                val storageDirectory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(storageDirectory, "${System.currentTimeMillis()}.png")
                try {
                    val stream: OutputStream = FileOutputStream(file)
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(currentContext.contentResolver, it)
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(currentContext.contentResolver, it)
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.flush()
                    stream.close()
                    return Single.just(file)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return Single.error(Throwable())
    }
}