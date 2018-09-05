package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.team.TeamResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.TeamView
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class TeamPresenter(val view: TeamView,
                    val apiRepository: ApiRepository,
                    val gson: Gson,
                    val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getClubs(leagueId: String){
        view.showTeamLoading()
        async(contextPool.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeams(leagueId)),
                        TeamResponse::class.java)
            }
            view.showTeamData(data.await().teams)
            view.hideTeamLoading()
        }
    }
}