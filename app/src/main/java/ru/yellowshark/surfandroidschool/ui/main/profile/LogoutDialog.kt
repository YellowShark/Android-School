package ru.yellowshark.surfandroidschool.ui.main.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import ru.yellowshark.surfandroidschool.R

class LogoutDialog(
    private val logoutListener: (() -> Unit)
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(ContextThemeWrapper(context!!, R.style.CustomAlertDialog))
            .setTitle(getString(R.string.really_want_to_quit))
            .setPositiveButton(R.string.log_out) { _: DialogInterface, _: Int ->
                logoutListener.invoke()
            }
            .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                //actually do nothing
            }
            .setCancelable(true)
            .create()
    }

}