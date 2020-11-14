package ru.yellowshark.surfandroidschool.ui.main.create

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import ru.yellowshark.surfandroidschool.R

class AddPictureDialogFragment(
    private val callingFragment: CreateMemeFragment,
    private val checkStorageCallback: () -> Boolean,
    private val checkCameraCallback: () -> Boolean,
) : DialogFragment() {

    private val CODE_GET_PHOTO: Int = 100

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
                    //open camera
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
}