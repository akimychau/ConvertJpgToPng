package ru.akimychev.convertjpgtopng.model

import android.net.Uri

fun interface ImageConverterRepository {

    fun convertToPng(uri: Uri?)
}

fun interface ImagePickerRepository {

    fun pickImage()
}

fun interface StoragePermissionRepository {
    fun requestPermission()
}
