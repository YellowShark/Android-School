package ru.yellowshark.surfandroidschool.ui.main.create

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.databinding.FragmentCreateMemeBinding
import ru.yellowshark.surfandroidschool.utils.getPhotoPath
import ru.yellowshark.surfandroidschool.utils.showErrorSnackbar
import ru.yellowshark.surfandroidschool.utils.viewBinding

class CreateMemeFragment : Fragment(R.layout.fragment_create_meme) {
    private val PERMISSION_CAMERA_REQUEST_CODE: Int = 1002
    private val PERMISSION_STORAGE_REQUEST_CODE: Int = 1001

    private val viewModel: CreateMemeViewModel by viewModel()
    private val binding: FragmentCreateMemeBinding by viewBinding(FragmentCreateMemeBinding::bind)
    private lateinit var itemCreate: MenuItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListeners()
    }

    private fun initView() {
        itemCreate = binding.toolbar.menu.findItem(R.id.action_create)
        binding.imageSrc = null
    }

    private fun initListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
            itemCreate.setOnMenuItemClickListener {
                if (isAllFilled(memeHeaderEt.text.toString())) {
                    viewModel.addMeme(
                        EntityLocalMeme(
                            createdDate = System.currentTimeMillis().toInt(),
                            description = memeTextEt.text.toString(),
                            isFavorite = false,
                            photoUrl = imageSrc.toString(),
                            title = memeHeaderEt.text.toString()
                        )
                    )
                    Toast.makeText(activity, "Ваш мем успешно создан!", Toast.LENGTH_SHORT).show()
                    this@CreateMemeFragment.fragmentManager?.popBackStack()
                }
                return@setOnMenuItemClickListener true
            }
            fabAddImage.setOnClickListener {
                showDialog()
            }
        }

    }

    private fun isAllFilled(text: String): Boolean {
        return when {
            text.isEmpty() -> {
                context?.applicationContext?.showErrorSnackbar(
                    binding.root,
                    getString(R.string.input_meme_title)
                )
                false
            }
            binding.imageSrc == null -> {
                context?.applicationContext?.showErrorSnackbar(
                    binding.root,
                    getString(R.string.load_photo)
                )
                false
            }
            else -> true
        }
    }

    private fun showDialog() {
        fragmentManager?.let {
            AddPictureDialogFragment(
                callingFragment = this,
                checkStorageCallback = {
                    if (isStoragePermissionsGranted())
                        return@AddPictureDialogFragment true
                    else
                        requestStoragePermissions()
                    return@AddPictureDialogFragment false
                },
                checkCameraCallback = {
                    if (isCameraPermissionGranted()) {
                        return@AddPictureDialogFragment true
                    } else {
                        requestCameraPermission()
                        return@AddPictureDialogFragment false
                    }
                }
            ).show(
                it,
                AddPictureDialogFragment::class.java.simpleName
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY_PHOTO -> {
                    Log.d("TAG", "onActivityResult: ${data?.data}")
                    data?.data?.let { uri ->
                        binding.imageSrc = uri.toString()
                    }
                }
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    binding.imageSrc = context?.applicationContext?.getPhotoPath()
                }
            }
        } else {
            Toast.makeText(activity, R.string.error_msg, Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //-------------------------------work with permissions--------------------------------------//

    private fun isStoragePermissionsGranted() = context?.applicationContext?.let {
        ContextCompat.checkSelfPermission(
            it, READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    it, WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    } ?: false

    private fun isCameraPermissionGranted() = context?.applicationContext?.let {
        ContextCompat.checkSelfPermission(
            it, CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    } ?: false

    private fun requestCameraPermission() {
        activity?.let { activity ->
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    CAMERA
                )
            ) {
                AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.camera_perm_rationale))
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            activity, arrayOf(
                                CAMERA
                            ),
                            PERMISSION_CAMERA_REQUEST_CODE
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        CAMERA
                    ),
                    PERMISSION_CAMERA_REQUEST_CODE
                )
            }
        }
    }

    private fun requestStoragePermissions() {
        activity?.let { activity ->
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.storage_perm_rationale))
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            activity, arrayOf(
                                WRITE_EXTERNAL_STORAGE,
                                READ_EXTERNAL_STORAGE,
                            ),
                            PERMISSION_STORAGE_REQUEST_CODE
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                    ),
                    PERMISSION_STORAGE_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_STORAGE_REQUEST_CODE -> {
                if (isStoragePermissionsGranted()) {
                    showDialog()
                } else {
                    context?.applicationContext?.let {
                        AlertDialog.Builder(it)
                            .setMessage(getString(R.string.you_can_change_it_in_options))
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (isCameraPermissionGranted()) {
                    showDialog()
                } else {
                    context?.applicationContext?.let {
                        AlertDialog.Builder(it)
                            .setMessage(getString(R.string.you_can_change_it_in_options))
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_GALLERY_PHOTO: Int = 1003
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: Int = 1004
    }
}