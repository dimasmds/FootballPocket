package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.player.PlayerResponse
import id.riotfallen.footballpocket.model.player.PlayersResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.PlayersView
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg

class PlayerPresenter(val view: PlayersView,
                      val apiRepository: ApiRepository,
                      val gson: Gson,
                      private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeamPlayers(teamId: String?) {
        view.showPlayerLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamPlayer(teamId)),
                    PlayerResponse::class.java)
        }
        launch(contextPool.main) {
            view.showPlayerData(data.await().player)
            view.hidePlayerLoading()
        }
    }

    fun getDetailPlayer(playerId: String?) {
        view.showPlayerLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getDetailPlayer(playerId)),
                    PlayersResponse::class.java)
        }
        launch(contextPool.main) {
            view.showPlayerData(data.await().players)
            view.hidePlayerLoading()
        }
    }
}