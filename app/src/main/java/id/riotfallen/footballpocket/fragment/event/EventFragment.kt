package id.riotfallen.footballpocket.fragment.event


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.recycler.event.EventListAdapter
import id.riotfallen.footballpocket.adapter.recycler.event.FavoriteListAdapter
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.db.database
import id.riotfallen.footballpocket.model.event.Event
import id.riotfallen.footballpocket.model.favorite.FavoriteEvent
import id.riotfallen.footballpocket.presenter.EventsPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.EventView
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.onRefresh


/**
 * A simple [Fragment] subclass.
 *
 */
class EventFragment : Fragment(), EventView {


    private var position: Int = 0
    private lateinit var leagueId: String

    private var favoriteEvents: MutableList<FavoriteEvent> = mutableListOf()

    private lateinit var eventPresenter: EventsPresenter

    private lateinit var eventListAdapter: EventListAdapter
    private lateinit var favoriteListAdapter: FavoriteListAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance(): EventFragment {
            return EventFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_event, container, false)

        position = arguments?.getInt("position") ?: 0
        leagueId = arguments?.getString("leagueId") ?: "0"

        progressBar = rootView.findViewById(R.id.eventFragmentProgressBar)
        swipeRefreshLayout = rootView.findViewById(R.id.eventFragmentSwipeRefresh)
        recyclerView = rootView.findViewById(R.id.eventFragmentRecyclerView)

        val request = ApiRepository()
        val gson = Gson()

        loadData(request, gson)

        swipeRefreshLayout.onRefresh {
            loadData(request, gson)
        }

        return rootView
    }

    private fun loadData(request: ApiRepository, gson: Gson) {
        when (position) {
            0 -> {
                eventPresenter = EventsPresenter(this, request, gson)
                eventPresenter.getPrevEventList(leagueId)
            }

            1 -> {
                eventPresenter = EventsPresenter(this, request, gson)
                eventPresenter.getNextEventList(leagueId)
            }

            2 -> {
                loadFavorite()
            }
        }
    }

    private fun loadFavorite() {
        swipeRefreshLayout.isRefreshing = true
        favoriteEvents.clear()
        context?.database?.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            favoriteEvents.addAll(favorite)
            showFavorite()
        }
    }

    private fun showFavorite() {
        swipeRefreshLayout.isRefreshing = false
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        favoriteListAdapter = FavoriteListAdapter(this.context, favoriteEvents as ArrayList<FavoriteEvent>)
        recyclerView.adapter = favoriteListAdapter
    }

    override fun showEventLoading() {
        progressBar.visible()
    }

    override fun hideEventLoading() {
        progressBar.invisible()
    }

    override fun showEventData(data: MutableList<Event>) {
        swipeRefreshLayout.isRefreshing = false
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        eventListAdapter = EventListAdapter(this.context, data)
        recyclerView.adapter = eventListAdapter
    }

}
