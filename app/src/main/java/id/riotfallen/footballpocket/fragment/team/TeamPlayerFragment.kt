package id.riotfallen.footballpocket.fragment.team

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.recycler.home.HomePlayerListAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.player.Player
import id.riotfallen.footballpocket.presenter.PlayerPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.PlayersView

class TeamPlayerFragment : Fragment(), PlayersView {


    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    private lateinit var teamId: String
    private lateinit var presenter: PlayerPresenter

    companion object {
        fun newInstance(): TeamPlayerFragment {
            return TeamPlayerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_team_player, container, false)

        progressBar = rootView.findViewById(R.id.teamPlayerProgressBar)
        recyclerView = rootView.findViewById(R.id.teamPlayerRecyclerView)

        teamId = arguments?.getString("teamId") ?: "0"

        val gson = Gson()
        val apiRepository = ApiRepository()

        presenter = PlayerPresenter(this, apiRepository, gson)
        presenter.getTeamPlayers(teamId)

        return rootView
    }

    override fun showPlayerLoading() {
        progressBar.visible()
        recyclerView.invisible()
    }

    override fun hidePlayerLoading() {
        progressBar.invisible()
        recyclerView.visible()
    }

    override fun showPlayerData(data: List<Player>) {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        val adapter = HomePlayerListAdapter(context, data as MutableList<Player>)
        recyclerView.adapter = adapter
    }
}
