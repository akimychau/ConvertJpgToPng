package ru.akimychev.convertjpgtopng

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImagePicker(
    private val converter: ConverterJpgToPng,
    registry: ActivityResultRegistry,
    callback: (imageUri: Uri?) -> Unit
) {

    private var getContent: ActivityResultLauncher<String> =
        registry.register(RESULT_REGISTRY_KEY, ActivityResultContracts.GetContent(), callback)

    fun selectImage() {
        getContent.launch("image/*")
    }

    fun convert(uri: Uri?) {
        converter.convertRx(uri)
    }

    private companion object {
        const val RESULT_REGISTRY_KEY = "pick_image"
    }
}

class ConverterJpgToPng(private val currentContext: Context) {
    fun convertRx(uri: Uri?) {
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}