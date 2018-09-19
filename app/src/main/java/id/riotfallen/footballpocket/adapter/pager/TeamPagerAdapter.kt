package id.riotfallen.footballpocket.adapter.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import id.riotfallen.footballpocket.fragment.team.TeamFavoriteFragment
import id.riotfallen.footballpocket.fragment.team.TeamFragment

class TeamPagerAdapter(fm: FragmentManager?,
                       private val noOfTabs: Int) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                TeamFragment.newInstance()
            }
            else -> TeamFavoriteFragment.newInstance()
        }
    }

    override fun getCount(): Int = noOfTabs
}