package one.mann.weatherman.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import one.mann.weatherman.ui.main.MainFragment

internal class MainPagerAdapter(fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var pages = 1

    override fun getItem(position: Int): Fragment = MainFragment.newInstance(position)

    override fun getCount(): Int = pages

    fun updatePages(newPages: Int) {
        pages = newPages
        notifyDataSetChanged()
    }
}