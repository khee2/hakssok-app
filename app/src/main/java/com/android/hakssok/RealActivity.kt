package com.android.hakssok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.android.hakssok.databinding.ActivityRealBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    val intent = Intent(this.intent)
                    startActivity(intent)
                }
                R.id.fragment_menu -> {
                    changeFragment(RestaurantFragment())
                }
                R.id.fragment_coupon -> {
                    changeFragment(CollegeFragment())
                }
            }
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContent.id, fragment).commit()
    }
}