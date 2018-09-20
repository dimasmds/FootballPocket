package id.riotfallen.footballpocket.adapter.recycler.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.activity.EventDetailActivity
import id.riotfallen.footballpocket.model.event.Event
import id.riotfallen.footballpocket.utils.BadgeFetcher
import id.riotfallen.footballpocket.utils.StringTools
import org.jetbrains.anko.startActivity

class HomeEventListAdapter(private val context: Context?,
                           private val events: MutableList<Event>)
    : RecyclerView.Adapter<HomeEventListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeEventListViewHolder {
        return HomeEventListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_item_home_event, parent, false))
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: HomeEventListViewHolder, position: Int) {
        holder.bindItem(events[position])
        holder.itemView.setOnClickListener {
            context?.startActivity<EventDetailActivity>("idEvent" to events[position].idEvent,
                    "idHome" to events[position].idHomeTeam, "idAway" to events[position].idAwayTeam)
        }
    }
}


class HomeEventListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textViewDate: TextView = view.findViewById(R.id.viheTextDate)

    private val imageViewHome: ImageView = view.findViewById(R.id.viheImageViewHome)
    private val textViewHomeName: TextView = view.findViewById(R.id.viheTextViewHome)
    private val textViewHomeScore: TextView = view.findViewById(R.id.viheTextViewScoreHome)

    private val imageViewAway: ImageView = view.findViewById(R.id.viheImageViewAway)
    private val textViewAwayName: TextView = view.findViewById(R.id.viheTextViewAway)
    private val textViewAwayScore: TextView = view.findViewById(R.id.viheTextViewScoreAway)

    fun bindItem(event: Event) {
        textViewDate.text = StringTools().dateToString(event.dateEvent)

        textViewHomeName.text = event.strHomeTeam?.toUpperCase() ?: ""
        textViewHomeScore.text = if (event.intHomeScore != null) event.intHomeScore.toString() else "V"
        event.idHomeTeam?.let { BadgeFetcher().loadBadges(it, imageViewHome) }

        textViewAwayName.text = event.strAwayTeam?.toUpperCase() ?: ""
        textViewAwayScore.text = if (event.intAwayScore != null) event.intAwayScore.toString() else "S"
        event.idAwayTeam?.let { BadgeFetcher().loadBadges(it, imageViewAway) }
    }

}
