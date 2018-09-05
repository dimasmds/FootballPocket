package id.riotfallen.footballpocket.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.HomeEventListAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.event.Event
import id.riotfallen.footballpocket.presenter.EventsPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.EventView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        EventView {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var leagueId: String
    private lateinit var presenter: EventsPresenter

    private lateinit var homeEventListAdapter: HomeEventListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainActivityToolbar)
        supportActionBar?.setIcon(R.drawable.ic_brand)
        supportActionBar?.title = ""

        actionBarDrawerToggle = ActionBarDrawerToggle(this, mainActivityDrawerLayout,
                R.string.open, R.string.close)

        mainActivityNavigationView.itemIconTintList = null
        mainActivityNavigationView.setNavigationItemSelectedListener(this)
        mainActivityNavigationView.inflateMenu(R.menu.navigation_menu)

        mainActivityDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        leagueId = "4328"

        val request = ApiRepository()
        val gson = Gson()

        presenter = EventsPresenter(this, request, gson)
        presenter.getNextEventList(leagueId)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menu_team -> {

            }

            R.id.menu_event -> {

            }
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
        mainProgressBarNextEvent.visible()
        mainRecyclerViewNextEvent.invisible()
    }

    override fun hideLoading() {
        mainProgressBarNextEvent.invisible()
        mainRecyclerViewNextEvent.visible()
    }

    override fun showEvent(data: List<Event>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainRecyclerViewNextEvent.layoutManager = layoutManager
        mainRecyclerViewNextEvent.itemAnimator = DefaultItemAnimator()
        homeEventListAdapter = HomeEventListAdapter(this, data as MutableList<Event>)
        mainRecyclerViewNextEvent.adapter = homeEventListAdapter
    }
}
