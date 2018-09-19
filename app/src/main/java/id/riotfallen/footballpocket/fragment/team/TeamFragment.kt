package id.riotfallen.footballpocket.fragment.team


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.recycler.team.TeamsAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.league.League
import id.riotfallen.footballpocket.model.team.Team
import id.riotfallen.footballpocket.presenter.LeaguesPresenter
import id.riotfallen.footballpocket.presenter.TeamPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.LeaguesView
import id.riotfallen.footballpocket.view.TeamView
import kotlinx.android.synthetic.main.fragment_team.*


class TeamFragment : Fragment(), TeamView, LeaguesView {


    private lateinit var gson: Gson
    private lateinit var request: ApiRepository

    private lateinit var leaguesPresenter: LeaguesPresenter

    private lateinit var leagueId: String

    private lateinit var adapter: TeamsAdapter

    companion object {
        fun newInstance(): TeamFragment {
            return TeamFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_team, container, false)

        gson = Gson()
        request = ApiRepository()

        leaguesPresenter = LeaguesPresenter(this, request, gson)
        leaguesPresenter.getLeagues()

        return rootView
    }

    override fun showTeamLoading() {
        teamFragmentSelectorProgressBar.visible()
        teamFragmentSelectorRecyclerView.invisible()
    }

    override fun hideTeamLoading() {
        teamFragmentSelectorProgressBar.invisible()
        teamFragmentSelectorRecyclerView.visible()
    }

    override fun showTeamData(data: List<Team>) {
        adapter = TeamsAdapter(context, data)
        val layoutManager = GridLayoutManager(context, 1)
        teamFragmentSelectorRecyclerView.layoutManager = layoutManager
        teamFragmentSelectorRecyclerView.adapter = adapter
    }

    override fun showLeagues(data: List<League>) {
        val arraySpinner: MutableList<String> = mutableListOf()
        for (index in data.indices) {
            if (index == 22) {
                break
            }

            data[index].strLeague?.let { arraySpinner.add(index, it) }
        }

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner)
        teamFragmentSelectorSpinner.adapter = arrayAdapter

        teamFragmentSelectorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                leagueId = data[teamFragmentSelectorSpinner.selectedItemPosition].idLeague.toString()
                val teamPresenter = TeamPresenter(this@TeamFragment, request, gson)
                teamPresenter.getTeams(leagueId)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun showDetailLeague(data: List<League>) {
    }
}
