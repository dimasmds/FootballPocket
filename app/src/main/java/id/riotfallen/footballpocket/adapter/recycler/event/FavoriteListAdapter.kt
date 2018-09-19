package id.riotfallen.footballpocket.adapter.recycler.event

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.activity.EventDetailActivity
import id.riotfallen.footballpocket.model.favorite.FavoriteEvent
import id.riotfallen.footballpocket.utils.BadgeFetcher
import id.riotfallen.footballpocket.utils.visible
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class FavoriteListAdapter(private val context: Context?, private val events: MutableList<FavoriteEvent>) :
        RecyclerView.Adapter<FavoriteListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        return FavoriteListViewHolder(LayoutInflater.from(context).inflate(R.layout.view_item_event_list, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.bindItem(events[position])
        holder.itemView.setOnClickListener {
            context?.startActivity<EventDetailActivity>("idEvent" to events[position].eventId,
                    "idHome" to events[position].homeId, "idAway" to events[position].awayId)
        }

    }

    override fun getItemCount(): Int = events.size
}

class FavoriteListViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val textViewHomeName : TextView = view.findViewById(R.id.textViewHomeClub)
    private val textViewHomeScore : TextView = view.findViewById(R.id.textViewHomeScore)

    private val textViewAwayName : TextView = view.findViewById(R.id.textViewAwayClub)
    private val textViewAwayScore : TextView = view.findViewById(R.id.textViewAwayScore)


    private val imageViewAwayLogo : ImageView = view.findViewById(R.id.imageViewAwayLogo)
    private val imageViewHomeLogo : ImageView = view.findViewById(R.id.imageViewHomeLogo)

    private val imageViewNotif: ImageView = view.findViewById(R.id.imageViewNotif)

    private val textViewDate : TextView = view.findViewById(R.id.textViewDateMatch)

    @SuppressLint("SimpleDateFormat")
    fun bindItem(match: FavoriteEvent) {

        textViewHomeName.text = match.homeName
        if (match.homeScore != null){
            textViewHomeScore.text = match.homeScore.toString()
        } else {
            textViewHomeScore.text = "V"
        }
        textViewAwayName.text = match.awayName

        if(match.awayScore != null){
            textViewAwayScore.text = match.awayScore.toString()
        } else {
            textViewAwayScore.text = "S"
        }

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(match.eventDate)
        val dateText = SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.getDefault())
                .format(date).toString()

        textViewDate.text = dateText

        match.awayId?.let { BadgeFetcher().loadBadges(it, imageViewAwayLogo) }
        match.homeId?.let { BadgeFetcher().loadBadges(it, imageViewHomeLogo) }

        if (!SimpleDateFormat("yyyy-MM-dd").parse(match.eventDate).before(Date())){
            imageViewNotif.visible()

            val cal = Calendar.getInstance()
            cal.time = date

            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.timeInMillis)
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.timeInMillis)
            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            intent.putExtra(CalendarContract.Events.TITLE, match.homeName + " VS " + match.awayName)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, match.homeName + " VS " + match.awayName)

            imageViewNotif.setOnClickListener { itemView.context.startActivity(intent) }
        }
    }

}
