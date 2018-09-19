package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.league.LeagueResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.LeaguesView
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg

class LeaguesPresenter(private val view: LeaguesView,
                       private val apiRepository: ApiRepository,
                       private val gson: Gson,
                       private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getLeagues() {
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getLeagues()),
                    LeagueResponse::class.java
            )
        }
        launch(contextPool.main) {

            view.showLeagues(data.await().leagues)
        }
    }


    fun getDetailLeagues(leagueId: String) {
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getDetailLeague(leagueId)),
                    LeagueResponse::class.java)
        }

        launch(contextPool.main) {
            view.showDetailLeague(data.await().leagues)
        }
    }
}
