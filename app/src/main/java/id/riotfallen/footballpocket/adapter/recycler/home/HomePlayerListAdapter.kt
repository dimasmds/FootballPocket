package id.riotfallen.footballpocket.adapter.recycler.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.activity.PlayerDetailActivity
import id.riotfallen.footballpocket.model.player.Player
import org.jetbrains.anko.startActivity

class HomePlayerListAdapter(private val context: Context?,
                            private val players: MutableList<Player>) :
        RecyclerView.Adapter<HomePlayerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePlayerListViewHolder {
        return HomePlayerListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_item_home_player, parent, false))
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: HomePlayerListViewHolder, position: Int) {
        holder.bindItem(players[position])
        holder.itemView.setOnClickListener {
            context?.startActivity<PlayerDetailActivity>("idPlayer" to players[position].idPlayer,
                    "namePlayer" to players[position].strPlayer)
        }
    }

}

class HomePlayerListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageViewPlayer: ImageView = view.findViewById(R.id.vihpImageViewPlayer)
    private val textViewNamePlayer: TextView = view.findViewById(R.id.vihpTextViewNamePlayer)
    private val textViewPositionPlayer: TextView = view.findViewById(R.id.vihpTextViewPositionPlayer)

    fun bindItem(player: Player) {
        textViewNamePlayer.text = player.strPlayer
        textViewPositionPlayer.text = player.strPosition
        Picasso.get().load(player.strCutout).into(imageViewPlayer)
    }
}
