package id.riotfallen.footballpocket.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.recycler.event.EventListAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.event.Event
import id.riotfallen.footballpocket.presenter.EventsPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.EventView
import kotlinx.android.synthetic.main.activity_event_search.*


class EventSearchActivity : AppCompatActivity(), EventView {


    private lateinit var eventPresenter: EventsPresenter
    private lateinit var eventListAdapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_search)
        setSupportActionBar(searchEventToolbar)
        supportActionBar?.title = "Search Event"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gson = Gson()
        val request = ApiRepository()

        eventPresenter = EventsPresenter(this, request, gson)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.menu_search)

        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isEmpty()) {
                    eventPresenter.searchEvent(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (!query.isEmpty()) {
                    eventPresenter.searchEvent(query)
                }
                return false
            }
        })
        searchView?.requestFocus()
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showEventLoading() {
        searchEventProgressBar.visible()
        searchEventRecyclerView.invisible()
    }

    override fun hideEventLoading() {
        searchEventProgressBar.invisible()
        searchEventRecyclerView.visible()
    }

    override fun showEventData(data: MutableList<Event>) {
        val filter: MutableList<Event> = mutableListOf()
        for (index in data.indices) {
            if (data[index].strSport.equals("Soccer")) {
                filter.add(data[index])
            }
        }
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        searchEventRecyclerView.layoutManager = layoutManager
        searchEventRecyclerView.itemAnimator = DefaultItemAnimator()
        eventListAdapter = EventListAdapter(this, filter)
        searchEventRecyclerView.adapter = eventListAdapter
    }
}
