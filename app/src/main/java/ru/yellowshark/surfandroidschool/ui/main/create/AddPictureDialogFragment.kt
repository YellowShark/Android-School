package ru.yellowshark.surfandroidschool.ui.main.create

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AddPictureDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf("Из галереи", "Сделать фото")
        return AlertDialog.Builder(context!!)
            .setTitle("Выбрать фото")
            .setCancelable(true)
            .setItems(items) { _: DialogInterface, which: Int ->
                Toast.makeText(context, items[which], Toast.LENGTH_SHORT).show()
            }
            .create()
    }

}