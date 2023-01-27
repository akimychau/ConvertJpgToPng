package ru.akimychev.convertjpgtopng.presenter

import android.net.Uri
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.akimychev.convertjpgtopng.model.ImageConverterRepository
import ru.akimychev.convertjpgtopng.model.ImagePickerRepository
import ru.akimychev.convertjpgtopng.model.StoragePermissionRepository
import ru.akimychev.convertjpgtopng.utils.subscribeByDefault
import ru.akimychev.convertjpgtopng.view.converterFragment.ConverterView
import java.util.concurrent.TimeUnit

class ConverterFragmentPresenter(
    private val imageConverterRepository: ImageConverterRepository,
    private val imagePickerRepository: ImagePickerRepository,
    private val permission: StoragePermissionRepository,
    private val router: Router
) : MvpPresenter<ConverterView>() {

    private val bag = CompositeDisposable()

    fun request() {
        permission.requestPermission()
    }

    fun convertAndSave(uri: Uri?) {
        viewState.showLoading()
        imageConverterRepository.convertToPngRx(uri)
            .delay(3, TimeUnit.SECONDS)
            .subscribeByDefault()
            .subscribe(
                {
                    viewState.hideLoading()
                    viewState.makeToastSuccess(it)
                },
                {
                    viewState.makeToastError()
                    viewState.hideLoading()
                }
            ).disposeBy(bag)
    }

    fun pickImage() {
        imagePickerRepository.pickImageRx()
            .subscribeByDefault()
            .subscribe(
                {
                    viewState.makeToastGallery(it)
                },
                {
                    viewState.makeToastError()
                })
            .disposeBy(bag)
    }

    private fun Disposable.disposeBy(bag: CompositeDisposable) {
        bag.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.dispose()
    }

    fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}