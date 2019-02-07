package one.mann.weatherman.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import one.mann.weatherman.view.WeatherFragment

class ViewPagerAdapter(fm: android.support.v4.app.FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var pages = 1

    override fun getItem(position: Int): Fragment = WeatherFragment.newInstance(position + 1)

    override fun getCount(): Int = pages

    fun updatePages(newPages: Int) {
        pages = newPages
        notifyDataSetChanged()
    }
}