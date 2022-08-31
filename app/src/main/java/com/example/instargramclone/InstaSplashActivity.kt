package com.example.instargramclone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class InstaSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_splash)

        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "empty")
        Log.d("loglog", token!!)
        when (token) {
            "empty" -> {
                // 로그인이 되어 있지 않은 경우
                startActivity(Intent(this, InstaLoginActivity::class.java))
            }
            else -> {
                // 로그인이 되어 있는 경우
                startActivity(Intent(this, InstarMainActivity::class.java))
            }
        }
    }
}