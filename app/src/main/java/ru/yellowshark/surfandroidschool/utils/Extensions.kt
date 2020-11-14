package ru.yellowshark.surfandroidschool.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.Meme
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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
        .setBackgroundTint(resources.getColor(R.color.red))
        .show()
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timestamp}",
        ".jpg",
        storageDir
    )
}
