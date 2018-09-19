package id.riotfallen.footballpocket.activity

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.pager.DetailTeamPagerAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.db.database
import id.riotfallen.footballpocket.model.favorite.FavoriteTeam
import id.riotfallen.footballpocket.model.team.Team
import id.riotfallen.footballpocket.presenter.TeamPresenter
import id.riotfallen.footballpocket.view.TeamView
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

class TeamDetailActivity : AppCompatActivity(), TeamView {


    private lateinit var team: Team

    private lateinit var gson: Gson
    private lateinit var apiRepository: ApiRepository

    private lateinit var teamId: String
    private lateinit var teamPresenter: TeamPresenter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    private lateinit var detailTeamPagerAdapter: DetailTeamPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        setSupportActionBar(teamDetailActivityToolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        teamId = intent.getStringExtra("teamId")

        teamDetailActivityTabLayout.addTab(teamDetailActivityTabLayout.newTab().setText("Description"))
        teamDetailActivityTabLayout.addTab(teamDetailActivityTabLayout.newTab().setText("Players"))

        detailTeamPagerAdapter = DetailTeamPagerAdapter(supportFragmentManager, teamDetailActivityTabLayout.tabCount, teamId)
        teamDetailActivityViewPager.adapter = detailTeamPagerAdapter
        teamDetailActivityViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(teamDetailActivityTabLayout))

        teamDetailActivityTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                teamDetailActivityViewPager.currentItem = tab.position
            }

        })

        gson = Gson()
        apiRepository = ApiRepository()

        teamPresenter = TeamPresenter(this, apiRepository, gson)
        teamPresenter.getTeamDetail(teamId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_team, menu)
        menuItem = menu

        favoriteState()
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item != null) {
            when (item.itemId) {
                android.R.id.home -> onBackPressed()
                R.id.add_to_favorite -> {
                    if (isFavorite) removeFromFavorite() else addToFavorite()
                    isFavorite = !isFavorite
                    setFavorite()
                }
            }
            true
        } else {
            false
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(FavoriteTeam.TABLE_FAVORITE_TEAM,
                        FavoriteTeam.TEAM_ID to teamId,
                        FavoriteTeam.TEAM_NAME to team.strTeam,
                        FavoriteTeam.TEAM_BADGE to team.strTeamBadge
                )
            }
            toast("Saved")
        } catch (e: SQLiteConstraintException) {
            toast(e.localizedMessage)
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(FavoriteTeam.TABLE_FAVORITE_TEAM,
                        "(TEAM_ID = {id})",
                        "id" to teamId)
            }
            toast("Removed from favorites")
        } catch (e: SQLiteConstraintException) {
            toast(e.localizedMessage)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun favoriteState() {
        database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to teamId)

            val save = result.parseList(classParser<FavoriteTeam>())
            if (!save.isEmpty()) isFavorite = true
        }
        setFavorite()
    }

    override fun showTeamLoading() {
    }

    override fun hideTeamLoading() {
    }

    override fun showTeamData(data: List<Team>) {
        team = data[0]
        Picasso.get().load(team.strTeamBadge).into(teamDetailActivityBadge)
        teamDetailActivityTeamName.text = team.strTeam
        teamDetailActivityWebsite.text = team.strWebsite
    }
}
