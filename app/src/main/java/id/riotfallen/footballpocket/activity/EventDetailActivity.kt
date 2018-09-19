package id.riotfallen.footballpocket.activity

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.db.database
import id.riotfallen.footballpocket.model.event.Event
import id.riotfallen.footballpocket.model.favorite.FavoriteEvent
import id.riotfallen.footballpocket.presenter.EventDetailPresenter
import id.riotfallen.footballpocket.utils.BadgeFetcher
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.EventView
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : AppCompatActivity(), EventView {

    private lateinit var event: Event

    private lateinit var presenter: EventDetailPresenter
    private lateinit var idEvent: String
    private lateinit var idAway: String
    private lateinit var idHome: String

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        idEvent = intent.getStringExtra("idEvent")
        idAway = intent.getStringExtra("idAway")
        idHome = intent.getStringExtra("idHome")

        setSupportActionBar(toolbarEventDetail)
        supportActionBar?.title = "Event Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val response = ApiRepository()
        val gson = Gson()

        presenter = EventDetailPresenter(this, response, gson)
        presenter.getEventDetail(idEvent)

        BadgeFetcher().loadBadges(idHome, imageViewBadgeHome)
        BadgeFetcher().loadBadges(idAway, imageViewBadgeAway)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_event, menu)
        menuItem = menu

        favoriteState()
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item != null) {
            when(item.itemId){
                android.R.id.home -> onBackPressed()
                R.id.add_to_favorite -> {
                    if(isFavorite) removeFromFavorite() else addToFavorite()

                    isFavorite =! isFavorite
                    setFavorite()
                }
            }
            true
        } else {
            false
        }
    }

    private fun setFavorite() {
        if(isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(FavoriteEvent.TABLE_FAVORITE_EVENT,
                        "(EVENT_ID = {id})",
                        "id" to idEvent)
            }
            toast("Removed from favorites")
        } catch (e : SQLiteConstraintException) {
            toast(e.localizedMessage)
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(FavoriteEvent.TABLE_FAVORITE_EVENT,
                        FavoriteEvent.EVENT_ID to idEvent,
                        FavoriteEvent.EVENT_DATE to event.dateEvent,
                        FavoriteEvent.HOME_ID to idHome,
                        FavoriteEvent.HOME_NAME to event.strHomeTeam,
                        FavoriteEvent.HOME_SCORE to event.intHomeScore,
                        FavoriteEvent.AWAY_ID to idAway,
                        FavoriteEvent.AWAY_NAME to event.strAwayTeam,
                        FavoriteEvent.AWAY_SCORE to event.intAwayScore
                )
            }
            toast("Added to Favorites")
        } catch (e : SQLiteConstraintException){
            toast(e.localizedMessage)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to idEvent)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if(!favorite.isEmpty()) isFavorite = true
        }
    }

    override fun showEventLoading() {
        progressbarEventDetail.visible()
        scrollViewEventDetail.invisible()
    }

    override fun hideEventLoading() {
        progressbarEventDetail.invisible()
        scrollViewEventDetail.visible()
    }

    override fun showEventData(data: MutableList<Event>) {
        event = data[0]

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(event.dateEvent)
        val dateText = SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.getDefault())
                .format(date).toString()

        textViewEventDate.text = dateText

        textViewHomeGoal.text = event.strHomeGoalDetails?.replace(";", "\n")
        textViewAwayGoal.text = event.strAwayGoalDetails?.replace(";", "\n")

        if(event.intHomeScore != null) textViewHomeScore.text = event.intHomeScore.toString()
        if(event.intAwayScore != null)textViewAwayScore.text = event.intAwayScore.toString()

        if(event.intHomeShots != null) TextViewShotHome.text = event.intHomeShots.toString()
        if(event.intAwayShots != null) TextViewShotAway.text = event.intAwayShots.toString()

        TextViewGKHome.text = event.strHomeLineupGoalkeeper
        TextViewGKAway.text = event.strAwayLineupGoalkeeper

        TextViewDefenseHome.text = event.strHomeLineupDefense?.replace("; ", "\n")
        TextViewDefenseAway.text = event.strAwayLineupDefense?.replace("; ", "\n")

        TextViewMidFieldHome.text = event.strHomeLineupMidfield?.replace("; ", "\n")
        TextViewMidFieldAway.text = event.strAwayLineupMidfield?.replace("; ", "\n")

        TextViewForwardHome.text = event.strHomeLineupForward?.replace("; ", "\n")
        TextViewForwardAway.text = event.strAwayLineupForward?.replace("; ", "\n")

        TextViewSubstitutesHome.text = event.strHomeLineupSubstitutes?.replace("; ", "\n")
        TextViewSubstitutesAway.text = event.strAwayLineupSubstitutes?.replace("; ", "\n")
    }
}
