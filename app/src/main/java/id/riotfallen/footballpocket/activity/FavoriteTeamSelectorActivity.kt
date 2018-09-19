package id.riotfallen.footballpocket.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.recycler.selector.TeamsSelectorAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.league.League
import id.riotfallen.footballpocket.model.team.Team
import id.riotfallen.footballpocket.presenter.LeaguesPresenter
import id.riotfallen.footballpocket.presenter.TeamPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.LeaguesView
import id.riotfallen.footballpocket.view.TeamView
import kotlinx.android.synthetic.main.activity_favorite_team_selector.*


class FavoriteTeamSelectorActivity : AppCompatActivity(), LeaguesView, TeamView{

    private lateinit var leaguesPresenter: LeaguesPresenter

    private lateinit var apiRepository: ApiRepository
    private lateinit var gson: Gson

    private lateinit var leagueId: String

    private lateinit var adapter: TeamsSelectorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_team_selector)
        setSupportActionBar(favoriteTeamSelectorToolbar)
        supportActionBar?.title = "Select a favorite team"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setWindowParams()

        apiRepository = ApiRepository()
        gson = Gson()

        leaguesPresenter = LeaguesPresenter(this, apiRepository, gson)
        leaguesPresenter.getLeagues()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun showLeagues(data: List<League>) {
        val arraySpinner: MutableList<String> = mutableListOf()
        for(index in data.indices){
            if (index == 22){
                break
            }

            data[index].strLeague?.let { arraySpinner.add(index, it) }
        }

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner)
        favoriteTeamSelectorSpinner.adapter = arrayAdapter

        favoriteTeamSelectorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                leagueId = data[favoriteTeamSelectorSpinner.selectedItemPosition].idLeague.toString()
                val teamPresenter = TeamPresenter(this@FavoriteTeamSelectorActivity, apiRepository, gson)
                teamPresenter.getTeams(leagueId)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun showDetailLeague(data: List<League>){}

    override fun showTeamLoading() {
        favoriteTeamSelectorProgressBar.visible()
        favoriteTeamSelectorRecyclerView.invisible()
    }

    override fun hideTeamLoading() {
        favoriteTeamSelectorProgressBar.invisible()
        favoriteTeamSelectorRecyclerView.visible()
    }

    override fun showTeamData(data: List<Team>) {
        adapter = TeamsSelectorAdapter(this, data)
        val layoutManager = GridLayoutManager(this, 1)
        favoriteTeamSelectorRecyclerView.layoutManager = layoutManager
        favoriteTeamSelectorRecyclerView.adapter = adapter
    }

    private fun setWindowParams() {
        val wlp = window.attributes
        wlp.dimAmount = 0.5f
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = wlp
    }
}
