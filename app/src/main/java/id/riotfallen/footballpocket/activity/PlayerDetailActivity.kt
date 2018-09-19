package id.riotfallen.footballpocket.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.player.Player
import id.riotfallen.footballpocket.presenter.PlayerPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.PlayersView
import kotlinx.android.synthetic.main.activity_detail_player.*

class PlayerDetailActivity : AppCompatActivity(), PlayersView {


    private lateinit var idPlayer: String
    private lateinit var namePlayer: String

    private lateinit var presenter: PlayerPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(detailPlayerActivityToolbar)
        idPlayer = intent.getStringExtra("idPlayer")
        namePlayer = intent.getStringExtra("namePlayer")
        supportActionBar?.title = namePlayer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gson = Gson()
        val request = ApiRepository()
        presenter = PlayerPresenter(this, request, gson)
        presenter.getDetailPlayer(idPlayer)
    }

    override fun showPlayerLoading() {
        detailPlayerActivityProgressBar.visible()
        detailPlayerActivityDescription.invisible()
    }

    override fun hidePlayerLoading() {
        detailPlayerActivityDescription.visible()
        detailPlayerActivityProgressBar.invisible()
    }

    override fun showPlayerData(data: List<Player>) {
        val player = data[0]

        Picasso.get().load(player.strFanart1).into(detailPlayerActivityImageBanner)
        detailPlayerActivityHeightPlayer.text = player.strHeight
        detailPlayerActivityWeightPlayer.text = player.strWeight
        detailPlayerActivityDescription.text = player.strDescriptionEN
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
