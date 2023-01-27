package ru.akimychev.convertjpgtopng.model

import android.net.Uri
import io.reactivex.rxjava3.core.Single

interface ImageConverterRepository {

    fun convertToPngRx(uri: Uri?): Single<String>
    fun convert(uri: Uri?): String
}

interface ImagePickerRepository {

    fun pickImageRx(): Single<String>
    fun launch(): String
}

fun interface StoragePermissionRepository {
    fun requestPermission()
}

