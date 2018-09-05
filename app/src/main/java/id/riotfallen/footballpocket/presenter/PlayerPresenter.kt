package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.player.PlayerResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.PlayersView
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class PlayerPresenter(val view: PlayersView,
                      val apiRepository: ApiRepository,
                      val gson: Gson,
                      val contextPool: CoroutineContextProvider = CoroutineContextProvider()){

    fun getTeamPlayers(teamId: String?) {
        view.showPlayerLoading()
        async(contextPool.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamPlayer(teamId)),
                        PlayerResponse::class.java)
            }

            view.showPlayerData(data.await().player)
            view.hidePlayerLoading()
        }
    }
}