package ru.akimychev.convertjpgtopng

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts

class ImagePicker(
    registry: ActivityResultRegistry,
    callback: (imageUri: Uri?) -> Unit
) {

    private var getContent: ActivityResultLauncher<String> =
        registry.register(RESULT_REGISTRY_KEY, ActivityResultContracts.GetContent(), callback)

    fun selectImage() {
        getContent.launch("image/*")
    }

    private companion object {
        const val RESULT_REGISTRY_KEY = "pick_image"
    }
}