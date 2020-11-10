package ru.yellowshark.surfandroidschool.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.ui.main.MemesActivity
import ru.yellowshark.surfandroidschool.utils.FORMATTED_PHONE_NUMBER_LENGTH
import ru.yellowshark.surfandroidschool.utils.MIN_PASSWORD_LENGTH


class AuthActivity: AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()
    private val stateObserver = Observer<ViewState> { state ->
        when(state) {
            is ViewState.Loading -> showProgressButton()
            is ViewState.Success -> {
                hideProgressButton()
                openMemesActivity()
            }
            is ViewState.Error -> showError()
        }
    }

    private fun openMemesActivity() {
        val intent = Intent(applicationContext, MemesActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = viewModel.getLastSessionToken()
        if (token != null) {
            openMemesActivity()
        }
        setContentView(R.layout.activity_auth)
        observeViewModel()
        initUi()
    }

    private fun observeViewModel() {
        viewModel.authViewState.observe(this, stateObserver)
    }

    private fun initUi() {
        val formatWatcher: FormatWatcher = MaskFormatWatcher(
            MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        )
        formatWatcher.installOn(login_et)
        login_et.addTextChangedListener {
            login_textInputLayout.isErrorEnabled = false
        }
        password_et.apply {
            setOnFocusChangeListener { _, hasFocus ->
                password_textInputLayout.apply {
                    endIconMode = if (hasFocus) {
                        TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    } else {
                        TextInputLayout.END_ICON_NONE
                    }
                }
            }
            addTextChangedListener {
                password_textInputLayout.apply {
                    if (this.isErrorEnabled)
                        isErrorEnabled = false
                    helperText =
                        if (it.toString().length >= MIN_PASSWORD_LENGTH) "" else getString(R.string.helper_text)
                }
            }
        }
        signIn_btn.setOnClickListener {
            if (isValid()) {
                viewModel.login(
                    login = "qwerty",
                    password = password_et.text.toString()
                )
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun showError() {
        hideProgressButton()
        Snackbar.make(rootAuth_constrainLayout, getString(R.string.error_msg), Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(R.color.red))
            .show()
    }

    private fun showProgressButton() {
        progressOnBtn_pb.visibility = View.VISIBLE
        signIn_btn.text = ""
    }

    private fun hideProgressButton() {
        progressOnBtn_pb.visibility = View.GONE
        signIn_btn.text = getString(R.string.sign_in)
    }

    private fun isValid(): Boolean {
        return when {
            login_et.text.toString().isEmpty() -> {
                login_textInputLayout.apply {
                    error = getString(R.string.error_empty_text)
                    isErrorEnabled = true
                }
                false
            }
            login_et.text.toString().length != FORMATTED_PHONE_NUMBER_LENGTH -> {
                login_textInputLayout.apply {
                    error = getString(R.string.error_text_is_not_phone_number)
                    isErrorEnabled = true
                }
                false
            }
            password_et.text.toString().isEmpty() -> {
                password_textInputLayout.apply {
                    error = getString(R.string.error_empty_text)
                    isErrorEnabled = true
                }
                false
            }
            password_et.text.toString().length < MIN_PASSWORD_LENGTH -> {
                password_textInputLayout.apply {
                    helperText = ""
                    error = getString(R.string.helper_text)
                    isErrorEnabled = true
                }
                false
            }
            else -> true
        }
    }
}