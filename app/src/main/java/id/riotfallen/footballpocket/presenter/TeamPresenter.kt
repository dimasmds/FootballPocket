package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.team.TeamResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.TeamView
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg

class TeamPresenter(val view: TeamView,
                    val apiRepository: ApiRepository,
                    val gson: Gson,
                    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeams(leagueId: String) {
        view.showTeamLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeams(leagueId)),
                    TeamResponse::class.java)
        }
        launch(contextPool.main) {
            view.showTeamData(data.await().teams)
            view.hideTeamLoading()
        }
    }

    fun getTeamDetail(teamId: String) {
        view.showTeamLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getDetailTeam(teamId)),
                    TeamResponse::class.java)
        }
        launch(contextPool.main) {
            view.showTeamData(data.await().teams)
            view.hideTeamLoading()
        }
    }

    fun searchTeam(keyword: String) {
        view.showTeamLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.searchTeam(keyword)),
                    TeamResponse::class.java)
        }
        launch(contextPool.main) {

            view.showTeamData(data.await().teams)
            view.hideTeamLoading()
        }

    }
}