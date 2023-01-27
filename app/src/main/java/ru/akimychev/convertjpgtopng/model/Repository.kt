package ru.akimychev.convertjpgtopng.model

import android.net.Uri
import io.reactivex.rxjava3.core.Single
import java.io.File

fun interface ImageConverterRepository {

    fun convertToPng(uri: Uri?): Single<File>
}

fun interface ImagePickerRepository {

    fun pickImage() : Single<String>
}

fun interface StoragePermissionRepository {
    fun requestPermission()
}
