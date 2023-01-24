package ru.akimychev.convertjpgtopng.view.converterFragment

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import coil.load
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.akimychev.convertjpgtopng.ConvertingApp
import ru.akimychev.convertjpgtopng.presenter.ConverterFragmentPresenter
import ru.akimychev.convertjpgtopng.core.OnBackPressedListener
import ru.akimychev.convertjpgtopng.databinding.FragmentMainBinding
import ru.akimychev.convertjpgtopng.model.impl.ImageConverterRepositoryImpl
import ru.akimychev.convertjpgtopng.model.impl.ImagePickerRepositoryImpl
import ru.akimychev.convertjpgtopng.model.impl.StoragePermissionRepositoryImpl

class ConverterFragment : MvpAppCompatFragment(), ConverterView, OnBackPressedListener {

    companion object {
        fun getInstance(): ConverterFragment {
            return ConverterFragment()
        }
    }

    private var uri: Uri? = null

    private val presenter: ConverterFragmentPresenter by moxyPresenter {
        ConverterFragmentPresenter(
            ImageConverterRepositoryImpl(requireContext()),
            ImagePickerRepositoryImpl(requireActivity().activityResultRegistry) { imageUri ->
                binding.img.load(imageUri)
                uri = imageUri
            },
            StoragePermissionRepositoryImpl(requireActivity().activityResultRegistry) { granted ->
                when {
                    granted -> {
                        presenter.pickImage()
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showDialogForRequestPermission()
                    }
                    else -> {
                        showDialogForClosedPermission()
                    }
                }
            },
            ConvertingApp.instance.router
        )
    }

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {
            presenter.request()
        }

        binding.btn2.setOnClickListener {
            presenter.convertAndSave(uri)
        }
    }

    override fun onBackPressed() = presenter.onBackPressed()

    override fun showDialogForRequestPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к галерее")
            .setMessage(
                "Если не разрешить доступ при следующем запросе, то разрешить его " +
                        "можно будет в любой момент в настройках телефона.\nХотите разрешить сейчас?"
            )
            .setPositiveButton("Да") { _, _ ->
                presenter.request()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showDialogForClosedPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к галерее закрыт")
            .setMessage("Чтобы открыть галерею разрешите доступ в настройках телефона")
            .setPositiveButton("Ок") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }
}
