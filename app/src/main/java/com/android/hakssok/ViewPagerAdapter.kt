package com.android.hakssok

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments: List<Fragment> = listOf(HomeFragment(), MyReviewFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyReviewFragment()
            1 -> HomeFragment() // TODO position1이 첫 화면이 되도록 조정
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}


