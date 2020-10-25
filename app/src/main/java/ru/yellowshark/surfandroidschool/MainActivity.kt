package ru.yellowshark.surfandroidschool

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ru.yellowshark.surfandroidschool.ui.AuthActivity

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