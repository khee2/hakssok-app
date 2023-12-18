package com.android.hakssok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("안으로 들어옴","응 들어옴")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro) // 레이아웃 리소스 ID를 지정

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
            Log.d("안으로 들어옴2","응 들어옴")
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            finish()
        }, 1000)
    }
}