package one.mann.weatherman.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import one.mann.weatherman.ui.main.MainFragment

internal class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var pages = 1

    override fun getItem(position: Int): Fragment = MainFragment.newInstance(position + 1)

    override fun getCount(): Int = pages

    fun updatePages(newPages: Int) {
        pages = newPages
        notifyDataSetChanged()
    }
}