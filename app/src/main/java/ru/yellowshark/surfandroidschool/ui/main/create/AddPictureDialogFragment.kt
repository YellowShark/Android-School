package ru.yellowshark.surfandroidschool.ui.main.create

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import ru.yellowshark.surfandroidschool.R

class AddPictureDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(ContextThemeWrapper(context!!, R.style.CustomAlertDialog))
            .setTitle("Выбрать фото")
            .setCancelable(true)
            .setPositiveButton("Из галереи") { _: DialogInterface, _: Int ->
                //not yet implemented
            }
            .setNegativeButton("Сделать фото") { _: DialogInterface, _: Int ->
                //not yet implemented
            }
            .create()
    }

}