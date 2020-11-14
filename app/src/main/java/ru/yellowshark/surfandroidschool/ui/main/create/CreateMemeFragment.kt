package ru.yellowshark.surfandroidschool.ui.main.create

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.databinding.FragmentCreateMemeBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateMemeFragment : Fragment(R.layout.fragment_create_meme) {

    companion object {
        val REQUEST_GALLERY_PHOTO: Int = 1003
    }

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: Int = 1004
    private val PERMISSION_CAMERA_REQUEST_CODE: Int = 1002
    private val PERMISSION_STORAGE_REQUEST_CODE: Int = 1001

    private val viewModel: CreateMemeViewModel by viewModel()

    private var _binding: FragmentCreateMemeBinding? = null
    private val binding get() = _binding!!

    private var _itemCreate: MenuItem? = null
    private val itemCreate get() = _itemCreate!!

    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_meme, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListeners()
    }

    private fun initView() {
        _itemCreate = binding.toolbar.menu.findItem(R.id.action_create)
        _itemCreate?.isEnabled = false
        //by default
        binding.imageSrc = null
    }

    private fun initListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
            itemCreate.setOnMenuItemClickListener {
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
                return@setOnMenuItemClickListener true
            }
            memeHeaderEt.addTextChangedListener { text ->
                _itemCreate?.isEnabled = text.toString().isNotEmpty()
            }
            fabAddImage.setOnClickListener {
                showDialog()
            }
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
                        dispatchTakePictureIntent()
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
                        setImageFromGallery(uri)
                    }
                }
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    setImageFromCamera()
                }
            }
        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setImageFromCamera() {
        activity?.let {
            Glide.with(it)
                .load(currentPhotoPath)
                .into(binding.memePicIv)
        }
    }

    private fun setImageFromGallery(uri: Uri) {
        activity?.let {
            Glide.with(it)
                .load(uri)
                .into(binding.memePicIv)
        }
    }


    //--------------camera pic--------------------------------------------------------------------//

    @Throws(IOException::class)
    private fun createImageFile(): File {
        context?.applicationContext?.let {
            val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File = it.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile(
                "JPEG_${timestamp}",
                ".jpg",
                storageDir
            ).apply {
                currentPhotoPath = absolutePath
            }
        }
        return File("")
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.applicationContext?.let { ctx ->
                takePictureIntent.resolveActivity(ctx.packageManager).also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                        null
                    }
                    photoFile?.also {
                        val photoUri: Uri = FileProvider.getUriForFile(
                            ctx,
                            "ru.yellowshark.surfandroidschool.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(
                            takePictureIntent,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                        )
                    }
                }
            }
        }
    }


    //--------------permissions-------------------------------------------------------------------//

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
                    dispatchTakePictureIntent()
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
}