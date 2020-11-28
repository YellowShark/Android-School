package ru.yellowshark.surfandroidschool.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.ActivityAuthBinding
import ru.yellowshark.surfandroidschool.domain.Error
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.ui.main.MemesActivity
import ru.yellowshark.surfandroidschool.utils.FORMATTED_PHONE_NUMBER_LENGTH
import ru.yellowshark.surfandroidschool.utils.MIN_PASSWORD_LENGTH
import ru.yellowshark.surfandroidschool.utils.showErrorSnackbar


class AuthActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModel()
    private val stateObserver = Observer<ViewState> { state ->
        when(state) {
            is ViewState.Loading -> showProgressButton()
            is ViewState.Success -> {
                hideProgressButton()
                openMemesActivity()
            }
            is ViewState.Error -> showError(state.error)
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
        observeViewModel()
        initUi()
    }

    private fun observeViewModel() {
        viewModel.authViewState.observe(this, stateObserver)
    }

    private fun initUi() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            val formatWatcher: FormatWatcher = MaskFormatWatcher(
                MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
            )
            formatWatcher.installOn(loginEt)
            loginEt.addTextChangedListener {
                loginTextInputLayout.isErrorEnabled = false
            }
            passwordEt.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    passwordTextInputLayout.apply {
                        endIconMode = if (hasFocus) {
                            TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        } else {
                            TextInputLayout.END_ICON_NONE
                        }
                    }
                }
                addTextChangedListener {
                    passwordTextInputLayout.apply {
                        if (this.isErrorEnabled)
                            isErrorEnabled = false
                        helperText =
                            if (it.toString().length >= MIN_PASSWORD_LENGTH) "" else getString(R.string.helper_text)
                    }
                }
            }
            signInBtn.setOnClickListener {
                if (isValid()) {
                    viewModel.login(
                        login = "qwerty", //потому что с помощью цифр нельзя ввести "qwerty" :)
                        password = passwordEt.text.toString()
                    )
                }
            }
        }
    }

    private fun showError(error: Error) {
        with(binding) {
            hideProgressButton()
            applicationContext.showErrorSnackbar(root, getErrorMessageText(error))
        }
    }

    private fun getErrorMessageText(error: Error) = when(error) {
        Error.SERVER_ERROR -> getString(R.string.error_fail_load_msg)
        Error.NO_INTERNET -> getString(R.string.error_no_internet)
    }

    private fun showProgressButton() {
        with(binding) {
            signInBtn.text = ""
            progressOnBtnPb.visibility = View.VISIBLE
        }
    }

    private fun hideProgressButton() {
        with(binding) {
            progressOnBtnPb.visibility = View.GONE
            signInBtn.text = getString(R.string.sign_in)
        }
    }

    private fun isValid(): Boolean {
        return with(binding) {
            when {
                loginEt.text.toString().isEmpty() -> {
                    loginTextInputLayout.apply {
                        error = getString(R.string.error_empty_text)
                        isErrorEnabled = true
                    }
                    false
                }
                loginEt.text.toString().length != FORMATTED_PHONE_NUMBER_LENGTH -> {
                    loginTextInputLayout.apply {
                        error = getString(R.string.error_text_is_not_phone_number)
                        isErrorEnabled = true
                    }
                    false
                }
                passwordEt.text.toString().isEmpty() -> {
                    passwordTextInputLayout.apply {
                        error = getString(R.string.error_empty_text)
                        isErrorEnabled = true
                    }
                    false
                }
                passwordEt.text.toString().length < MIN_PASSWORD_LENGTH -> {
                    passwordTextInputLayout.apply {
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
}