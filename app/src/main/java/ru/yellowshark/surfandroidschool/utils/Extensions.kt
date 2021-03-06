package ru.yellowshark.surfandroidschool.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.ui.main.create.AddPictureDialogFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun <T> Single<T>.runInBackground() =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.runInBackground() =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Activity.shareMeme(meme: Meme) {
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            meme.title + "\n" + meme.photoUrl + "\n" + meme.description
        )
        type = "text/plain"
    }
    ContextCompat.startActivity(
        this,
        Intent.createChooser(shareIntent, this.resources.getText(R.string.send_to)),
        null
    )
}

fun Context.showErrorSnackbar(root: View, text: String) {
    Snackbar.make(root, text, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(this, R.color.red))
        .show()
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timestamp}",
        ".jpg",
        storageDir
    )
}

fun Context.savePhotoPath(currentPhotoPath: String) {
    this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE)
        ?.let { preferences ->
            val editor = preferences.edit()
            editor.putString(AddPictureDialogFragment.PHOTO_PATH_KEY, currentPhotoPath)
            editor.apply()
        }
}

fun Context.getPhotoPath(): String {
    return this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE)
        ?.getString(AddPictureDialogFragment.PHOTO_PATH_KEY, "") ?: ""
}
