package ru.akimychev.convertjpgtopng.model.impl

import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import io.reactivex.rxjava3.core.Single
import ru.akimychev.convertjpgtopng.model.ImagePickerRepository

class ImagePickerRepositoryImpl(
    registry: ActivityResultRegistry,
    callback: (imageUri: Uri?) -> Unit
) : ImagePickerRepository {

    private var getContent: ActivityResultLauncher<String> =
        registry.register(RESULT_REGISTRY_KEY, ActivityResultContracts.GetContent(), callback)

    override fun launch(): String {
        Log.d("@@@", Thread.currentThread().name)
        getContent.launch("image/*")
        return "Выберите изображение"
    }

    override fun pickImageRx(): Single<String> {
        return Single.create {
            it.onSuccess(
                launch()
            )
        }
    }

    private companion object {
        const val RESULT_REGISTRY_KEY = "pick_image"
    }
}