package com.android.hakssok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.hakssok.databinding.ActivityRealBinding

class RealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewpagerReal.adapter = ViewPagerAdapter(this) // 어댑터 생성
    }
}