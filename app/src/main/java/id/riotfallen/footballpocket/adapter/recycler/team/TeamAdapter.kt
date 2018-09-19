package id.riotfallen.footballpocket.adapter.recycler.team

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.activity.TeamDetailActivity
import id.riotfallen.footballpocket.model.team.Team
import org.jetbrains.anko.*

class TeamsAdapter (private val context: Context?, private val teams : List<Team>) : RecyclerView.Adapter<TeamViewHolder>() {

    override fun onBindViewHolder(holderSelectorFavorite: TeamViewHolder, position: Int) {
        holderSelectorFavorite.bindItem(teams[position])
        holderSelectorFavorite.itemView.setOnClickListener {
            context?.startActivity<TeamDetailActivity>("teamId" to teams[position].idTeam)
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
                        textColor = Color.BLACK
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