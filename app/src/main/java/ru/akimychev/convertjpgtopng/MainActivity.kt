package ru.akimychev.convertjpgtopng

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import ru.akimychev.convertjpgtopng.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val imagePicker = ImagePicker(activityResultRegistry) { imageUri ->
        binding.img.load(imageUri)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    imagePicker.selectImage()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
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
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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