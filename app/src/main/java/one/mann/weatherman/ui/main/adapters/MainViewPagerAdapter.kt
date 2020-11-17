package one.mann.weatherman.ui.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import one.mann.weatherman.ui.main.CityFragment

/* Created by Psmann. */

class MainViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    private var pageCount = 1

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment = CityFragment.newInstance(position)

    fun updatePages(newCount: Int) {
        pageCount = newCount
        notifyDataSetChanged()
    }
}