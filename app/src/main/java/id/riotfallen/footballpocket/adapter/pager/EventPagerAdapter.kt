package id.riotfallen.footballpocket.adapter.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import id.riotfallen.footballpocket.fragment.EventFragment

class EventPagerAdapter (fm: FragmentManager?,
                         private val noOfTabs: Int,
                         private val idLeague: String) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        val b = Bundle()
        b.putInt("position", position)
        b.putString("leagueId", idLeague)
        val frag = EventFragment.newInstance()
        frag.arguments = b
        return frag
    }

    override fun getCount(): Int  = noOfTabs
}