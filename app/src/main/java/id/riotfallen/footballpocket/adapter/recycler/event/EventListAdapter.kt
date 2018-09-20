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
import id.riotfallen.footballpocket.model.event.Event
import id.riotfallen.footballpocket.utils.BadgeFetcher
import id.riotfallen.footballpocket.utils.visible
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter(private val context: Context?,
                       private val events: MutableList<Event>)
    : RecyclerView.Adapter<EventListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        return EventListViewHolder(LayoutInflater.from(context).inflate(R.layout.view_item_event_list, parent, false))
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        holder.bindItem(events[position])
        holder.itemView.setOnClickListener {
            context?.startActivity<EventDetailActivity>("idEvent" to events[position].idEvent,
                    "idHome" to events[position].idHomeTeam, "idAway" to events[position].idAwayTeam)
        }
    }
}

class EventListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textViewHomeName: TextView = view.findViewById(R.id.textViewHomeClub)
    private val textViewHomeScore: TextView = view.findViewById(R.id.textViewHomeScore)

    private val textViewAwayName: TextView = view.findViewById(R.id.textViewAwayClub)
    private val textViewAwayScore: TextView = view.findViewById(R.id.textViewAwayScore)

    private val imageViewAwayLogo: ImageView = view.findViewById(R.id.imageViewAwayLogo)
    private val imageViewHomeLogo: ImageView = view.findViewById(R.id.imageViewHomeLogo)

    private val imageViewNotif: ImageView = view.findViewById(R.id.imageViewNotif)

    private val textViewDate: TextView = view.findViewById(R.id.textViewDateMatch)

    @SuppressLint("SimpleDateFormat")
    fun bindItem(event: Event) {
        textViewHomeName.text = event.strHomeTeam
        if (event.intHomeScore != null) {
            textViewHomeScore.text = event.intHomeScore.toString()
        } else {
            textViewHomeScore.text = "V"
        }
        textViewAwayName.text = event.strAwayTeam

        if (event.intAwayScore != null) {
            textViewAwayScore.text = event.intAwayScore.toString()
        } else {
            textViewAwayScore.text = "S"
        }

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(event.dateEvent)
        val dateText = SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.getDefault())
                .format(date).toString()

        textViewDate.text = dateText

        event.idAwayTeam?.let { BadgeFetcher().loadBadges(it, imageViewAwayLogo) }
        event.idHomeTeam?.let { BadgeFetcher().loadBadges(it, imageViewHomeLogo) }

        if (!SimpleDateFormat("yyyy-MM-dd").parse(event.dateEvent).before(Date())) {
            imageViewNotif.visible()

            val cal = Calendar.getInstance()
            cal.time = date

            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.timeInMillis)
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.timeInMillis)
            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            intent.putExtra(CalendarContract.Events.TITLE, event.strAwayTeam + " VS " + event.strHomeTeam)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, event.strAwayTeam + " VS " + event.strHomeTeam)

            imageViewNotif.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    itemView.context.startActivity(intent)
                }

            })
        }

    }

}