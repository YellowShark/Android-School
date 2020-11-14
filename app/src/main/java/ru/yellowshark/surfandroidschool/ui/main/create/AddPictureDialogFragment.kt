package ru.yellowshark.surfandroidschool.ui.main.create

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextThemeWrapper
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.utils.APP_AUTHORITY
import ru.yellowshark.surfandroidschool.utils.createImageFile
import java.io.File
import java.io.IOException

class AddPictureDialogFragment(
    private val callingFragment: CreateMemeFragment,
    private val checkStorageCallback: () -> Boolean,
    private val checkCameraCallback: () -> Boolean,
) : DialogFragment() {

    companion object {
        val PHOTO_PATH_KEY = "PHOTO_PATH_KEY"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(ContextThemeWrapper(context!!, R.style.CustomAlertDialog))
            .setTitle("Выбрать фото")
            .setCancelable(true)
            .setPositiveButton("Из галереи") { _: DialogInterface, _: Int ->
                if (checkStorageCallback.invoke()) {
                    selectPhotoFromGallery()
                }
            }
            .setNegativeButton("Сделать фото") { _: DialogInterface, _: Int ->
                if (checkCameraCallback.invoke()) {
                    dispatchTakePictureIntent()
                }
            }
            .create()
    }

    private fun selectPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        callingFragment.startActivityForResult(intent, CreateMemeFragment.REQUEST_GALLERY_PHOTO)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.applicationContext?.let { ctx ->
                takePictureIntent.resolveActivity(ctx.packageManager).also {
                    val photoFile: File? = try {
                        ctx.createImageFile()
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                        null
                    }
                    photoFile?.also { file ->
                        savePhotoPath(file.absolutePath)
                        val photoUri: Uri = FileProvider.getUriForFile(
                            ctx,
                            APP_AUTHORITY,
                            file
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        callingFragment.startActivityForResult(
                            takePictureIntent,
                            CreateMemeFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                        )
                    }
                }
            }
        }
    }

    private fun savePhotoPath(currentPhotoPath: String) {
        context?.getSharedPreferences(context?.getString(R.string.app_name), Context.MODE_PRIVATE)?.let { preferences ->
            val editor = preferences.edit()
            editor.putString(PHOTO_PATH_KEY, currentPhotoPath)
            editor.apply()
        }
    }
}