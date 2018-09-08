package id.riotfallen.footballpocket.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.pager.EventPagerAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.league.League
import id.riotfallen.footballpocket.presenter.LeaguesPresenter
import id.riotfallen.footballpocket.view.LeaguesView
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : AppCompatActivity(), LeaguesView {


    private lateinit var presenter: LeaguesPresenter
    private lateinit var eventPagerAdapter: EventPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        setSupportActionBar(eventActivityToolbar)

        supportActionBar?.title = "Event List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventActivityTabLayout.addTab(eventActivityTabLayout.newTab().setText("Prev Event"))
        eventActivityTabLayout.addTab(eventActivityTabLayout.newTab().setText("Next Event"))
        eventActivityTabLayout.addTab(eventActivityTabLayout.newTab().setText("Favorites Event"))

        val request = ApiRepository()
        val gson = Gson()
        presenter = LeaguesPresenter(this, request, gson)
        presenter.getLeagues()
    }

    override fun showLeagues(data: List<League>) {
        val arraySpinner: MutableList<String> = mutableListOf()
        for (index in data.indices) {
            if(index == 22){
                break
            }
            data[index].strLeague?.let { arraySpinner.add(index, it) }
        }

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this,
                R.layout.spinner_item, arraySpinner)

        eventActivitySpinner.adapter = arrayAdapter

        eventActivityTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                eventActivityViewPager.currentItem = tab.position
            }

        })

        eventActivitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val idLeague = data[eventActivitySpinner.selectedItemPosition].idLeague
                eventPagerAdapter = EventPagerAdapter(supportFragmentManager, eventActivityTabLayout.tabCount, idLeague!!)
                eventActivityViewPager.adapter = eventPagerAdapter
                eventActivityViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(eventActivityTabLayout))
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showDetailLeague(data: List<League>) {

    }
}
