package ru.akimychev.convertjpgtopng.model.impl

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import ru.akimychev.convertjpgtopng.model.ImagePickerRepository

class ImagePickerRepositoryImpl(
    registry: ActivityResultRegistry,
    callback: (imageUri: Uri?) -> Unit
) : ImagePickerRepository {

    private var getContent: ActivityResultLauncher<String> =
        registry.register(RESULT_REGISTRY_KEY, ActivityResultContracts.GetContent(), callback)

    override fun pickImage() {
        getContent.launch("image/*")
    }

    private companion object {
        const val RESULT_REGISTRY_KEY = "pick_image"
    }
}