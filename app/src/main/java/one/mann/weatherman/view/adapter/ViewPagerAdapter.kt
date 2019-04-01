package one.mann.weatherman.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import one.mann.weatherman.view.WeatherFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var pages = 1

    override fun getItem(position: Int): Fragment = WeatherFragment.newInstance(position + 1)

    override fun getCount(): Int = pages

    fun updatePages(newPages: Int) {
        pages = newPages
        notifyDataSetChanged()
    }
}