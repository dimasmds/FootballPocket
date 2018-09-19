package id.riotfallen.footballpocket.adapter.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import id.riotfallen.footballpocket.fragment.team.TeamDescFragment
import id.riotfallen.footballpocket.fragment.team.TeamPlayerFragment

class DetailTeamPagerAdapter(fm: FragmentManager?,
                             private val noOfTabs: Int,
                             private val teamId: String) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        val b = Bundle()
        b.putString("teamId", teamId)
        return when (position) {
            0 -> {
                val frag = TeamDescFragment.newInstance()
                frag.arguments = b
                frag
            }
            else -> {
                val frag = TeamPlayerFragment.newInstance()
                frag.arguments = b
                frag
            }
        }
    }

    override fun getCount(): Int = noOfTabs
}