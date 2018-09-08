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
import id.riotfallen.footballpocket.model.favorite.Favorite
import id.riotfallen.footballpocket.utils.BadgeFetcher
import id.riotfallen.footballpocket.utils.StringTools
import org.jetbrains.anko.startActivity

class HomeFavoriteEventListAdapter(private val context: Context?,
                           private val favorites: MutableList<Favorite>)
    : RecyclerView.Adapter<HomeFavoriteEventListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFavoriteEventListViewHolder {
        return HomeFavoriteEventListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_item_home_event, parent, false))
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: HomeFavoriteEventListViewHolder, position: Int) {
        holder.bindItem(favorites[position])
        holder.itemView.setOnClickListener {
            context?.startActivity<EventDetailActivity>("idEvent" to favorites[position].eventId,
                    "idHome" to favorites[position].homeId, "idAway" to favorites[position].awayId)
        }
    }
}


class HomeFavoriteEventListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textViewDate: TextView = view.findViewById(R.id.viheTextDate)

    private val imageViewHome: ImageView = view.findViewById(R.id.viheImageViewHome)
    private val textViewHomeName: TextView = view.findViewById(R.id.viheTextViewHome)
    private val textViewHomeScore: TextView = view.findViewById(R.id.viheTextViewScoreHome)

    private val imageViewAway: ImageView = view.findViewById(R.id.viheImageViewAway)
    private val textViewAwayName: TextView = view.findViewById(R.id.viheTextViewAway)
    private val textViewAwayScore: TextView = view.findViewById(R.id.viheTextViewScoreAway)

    fun bindItem(event: Favorite){
        textViewDate.text = StringTools().DateToString(event.eventDate)

        textViewHomeName.text = event.homeName?.toUpperCase() ?: ""
        textViewHomeScore.text = if(event.homeScore != null) event.homeScore.toString() else "V"
        event.homeId?.let { BadgeFetcher().loadBadges(it, imageViewHome) }

        textViewAwayName.text = event.awayName?.toUpperCase() ?: ""
        textViewAwayScore.text = if(event.awayScore != null) event.awayScore.toString() else "S"
        event.awayId?.let { BadgeFetcher().loadBadges(it, imageViewAway) }
    }

}
