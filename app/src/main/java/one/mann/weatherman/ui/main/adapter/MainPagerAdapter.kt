package one.mann.weatherman.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import one.mann.weatherman.ui.main.CityFragment

/* Created by Psmann. */

@Suppress("DEPRECATION") // BEHAVIOR_SET_USER used because BEHAVIOR_RESUME_ONLY has issues
internal class MainPagerAdapter(fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    private var pages = 1
    private var fragmentRemoved = false

    override fun getItem(position: Int): Fragment = CityFragment.newInstance(position)

    override fun getCount(): Int = pages

    override fun getItemPosition(`object`: Any): Int =
            if (fragmentRemoved) PagerAdapter.POSITION_NONE // Force adapter to reload all fragments
            else PagerAdapter.POSITION_UNCHANGED

    fun updatePages(newPages: Int) {
        fragmentRemoved = newPages < pages // True if a fragment is removed
        pages = newPages
        notifyDataSetChanged()
    }
}