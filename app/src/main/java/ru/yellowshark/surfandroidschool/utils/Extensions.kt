package ru.yellowshark.surfandroidschool.utils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.Meme

fun Context.shareMeme(meme: Meme) {
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