package ru.yellowshark.surfandroidschool.ui.auth

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.utils.FORMATTED_PHONE_NUMBER_LENGTH
import ru.yellowshark.surfandroidschool.utils.MIN_PASSWORD_LENGTH


class AuthActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initUi()
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
        signIn_btn.setOnClickListener { userAuth() }
    }

    private fun userAuth() {
        showProgressButton()

        //это здесь временно!!
        MemesApi.getInstance()
            .userAuth(AuthRequest(login = "qwerty", password = "qwerty")).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    Log.d("TAG", "onResponse:\n" +
                            "code: ${response.code()}\n" +
                            "message: ${response.message()}\n" +
                            "body: ${response.body()}")

                    if (response.isSuccessful && response.body()?.accessToken != null) {
                        Toast.makeText(this@AuthActivity, getString(R.string.success_msg), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@AuthActivity, getString(R.string.error_msg), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        Handler().postDelayed(hideProgressButton(), 1000)
        when {
            login_et.text.toString().isEmpty() -> {
                login_textInputLayout.apply {
                    error = getString(R.string.error_empty_text)
                    isErrorEnabled = true
                }
            }
            login_et.text.toString().length != FORMATTED_PHONE_NUMBER_LENGTH -> {
                login_textInputLayout.apply {
                    error = getString(R.string.error_text_is_not_phone_number)
                    isErrorEnabled = true
                }
            }
            password_et.text.toString().isEmpty() -> {
                password_textInputLayout.apply {
                    error = getString(R.string.error_empty_text)
                    isErrorEnabled = true
                }
            }
            password_et.text.toString().length < MIN_PASSWORD_LENGTH -> {
                password_textInputLayout.apply {
                    helperText = ""
                    error = getString(R.string.helper_text)
                    isErrorEnabled = true
                }
            }
            else -> { /*TODO network request } */
            }
        }
    }

    private fun showProgressButton() {
        progressOnBtn_pb.visibility = View.VISIBLE
        signIn_btn.text = ""
    }

    private fun hideProgressButton() = Runnable {
        progressOnBtn_pb.visibility = View.GONE
        signIn_btn.text = getString(R.string.sign_in)
    }

}