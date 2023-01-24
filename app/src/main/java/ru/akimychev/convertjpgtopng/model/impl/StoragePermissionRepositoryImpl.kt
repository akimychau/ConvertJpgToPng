package ru.akimychev.convertjpgtopng.model.impl

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import ru.akimychev.convertjpgtopng.model.StoragePermissionRepository

class StoragePermissionRepositoryImpl(
    registry: ActivityResultRegistry,
    callback: (granted: Boolean) -> Unit
) : StoragePermissionRepository {

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registry.register(
            PERMISSION_REGISTRY_KEY,
            ActivityResultContracts.RequestPermission(),
            callback
        )

    private companion object {
        const val PERMISSION_REGISTRY_KEY = "permission"
    }

    override fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}