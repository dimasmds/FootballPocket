package id.riotfallen.footballpocket.adapter.recycler

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.riotfallen.footballpocket.model.team.Team
import org.jetbrains.anko.*
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.utils.PrefConfig

class TeamsAdapter (private val context: Context?, private val teams : List<Team>) : RecyclerView.Adapter<TeamViewHolder>() {

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindItem(teams[position])
        holder.itemView.setOnClickListener { it ->
            context?.alert("Make ${teams[position].strTeam} your favorite team?") {
                yesButton {
                    teams[position].idTeam?.let { it1 -> PrefConfig(context).writeIdTeam(it1) }
                    (context as Activity).onBackPressed()
                }
                noButton {  context.toast("ok then you cancelled!") }
            }?.show()
        }
    }

    override fun getItemCount(): Int = teams.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    class TeamUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            return with(ui) {
                linearLayout {
                    lparams(width = matchParent, height = wrapContent)
                    padding = dip(16)
                    orientation = LinearLayout.HORIZONTAL

                    imageView {
                        id = R.id.team_badge
                    }.lparams{
                        height = dip(50)
                        width = dip(50)
                    }

                    textView {
                        id = R.id.team_name
                        textSize = 16f
                    }.lparams{
                        margin = dip(15)
                    }

                }
            }
        }

    }

}

class TeamViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val teamBadge : ImageView = view.findViewById(R.id.team_badge)
    private val teamName : TextView = view.findViewById(R.id.team_name)

    fun bindItem(teams: Team){
        Picasso.get().load(teams.strTeamBadge).into(teamBadge)
        teamName.text = teams.strTeam
    }

}