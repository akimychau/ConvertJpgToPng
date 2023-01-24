package ru.akimychev.convertjpgtopng.presenter

import android.net.Uri
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.akimychev.convertjpgtopng.model.ImageConverterRepository
import ru.akimychev.convertjpgtopng.model.ImagePickerRepository
import ru.akimychev.convertjpgtopng.model.StoragePermissionRepository
import ru.akimychev.convertjpgtopng.view.converterFragment.ConverterView

class ConverterFragmentPresenter(
    private val imageConverterRepository: ImageConverterRepository,
    private val imagePickerRepository: ImagePickerRepository,
    private val permission: StoragePermissionRepository,
    private val router: Router
) : MvpPresenter<ConverterView>() {

    fun request() {
        permission.requestPermission()
    }

    fun convertAndSave(uri: Uri?) {
        imageConverterRepository.convertToPng(uri)
    }

    fun pickImage() {
        imagePickerRepository.pickImage()
    }

    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}