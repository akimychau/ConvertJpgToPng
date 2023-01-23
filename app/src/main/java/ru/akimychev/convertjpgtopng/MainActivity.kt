package ru.akimychev.convertjpgtopng

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import ru.akimychev.convertjpgtopng.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var uri: Uri? = null
    private val imagePicker =
        ImagePicker(ConverterJpgToPng(this), activityResultRegistry) { imageUri ->
            binding.img.load(imageUri)
            uri = imageUri
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    imagePicker.selectImage()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    showDialogForRequestPermission()
                }
                else -> {
                    showDialogForClosedPermission()
                }
            }

            Log.d("@@@", "Permission granted: $granted")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            requestPermission()
        }

        binding.btn2.setOnClickListener {

            imagePicker.convert(uri)
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun showDialogForRequestPermission() {
        AlertDialog.Builder(this)
            .setTitle("Доступ к галерее")
            .setMessage(
                "Если не разрешить доступ при следующем запросе, то разрешить его " +
                        "можно будет в любой момент в настройках телефона.\nХотите разрешить сейчас?"
            )
            .setPositiveButton("Да") { _, _ ->
                requestPermission()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showDialogForClosedPermission() {
        AlertDialog.Builder(this)
            .setTitle("Доступ к галерее закрыт")
            .setMessage("Чтобы открыть галерею разрешите доступ в настройках телефона")
            .setPositiveButton("Ок") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}