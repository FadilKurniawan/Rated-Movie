package com.devfk.ratedmovie.feature

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devfk.ratedmovie.feature.bookmark.MyListFragment
import com.devfk.ratedmovie.feature.home.HomeFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> HomeFragment()
        1 -> MyListFragment()
        else -> MyListFragment()
    }
    override fun getCount(): Int = 3
}