package id.riotfallen.footballpocket.fragment.team

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.recycler.team.FavoriteTeamAdapter
import id.riotfallen.footballpocket.db.database
import id.riotfallen.footballpocket.model.favorite.FavoriteTeam
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class TeamFavoriteFragment : Fragment() {

    private var favoritesTeam: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var favoriteTeamAdapter: FavoriteTeamAdapter
    private lateinit var recylerView: RecyclerView

    companion object {
        fun newInstance(): TeamFavoriteFragment {
            return TeamFavoriteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_team_favorite, container, false)
        recylerView = rootView.findViewById(R.id.teamFavFragmentRecyclerView)
        loadData()
        return rootView
    }

    private fun loadData() {
        favoritesTeam.clear()
        context?.database?.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            favoritesTeam.addAll(favorite)
            showFavorites()
        }
    }

    private fun showFavorites() {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 1)
        recylerView.layoutManager = layoutManager
        recylerView.itemAnimator = DefaultItemAnimator()
        favoriteTeamAdapter = FavoriteTeamAdapter(context, favoritesTeam)
        recylerView.adapter = favoriteTeamAdapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
