package ru.yellowshark.surfandroidschool.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, AuthActivity::class.java)
            startActivity(intent)
            this.finish()
        }, 300)
    }
}