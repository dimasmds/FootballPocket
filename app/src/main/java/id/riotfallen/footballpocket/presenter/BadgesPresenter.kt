package id.riotfallen.footballpocket.presenter

import android.widget.ImageView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.team.TeamResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class BadgesPresenter(private val view: ImageView,
                      private val apiRepository: ApiRepository,
                      private val gson: Gson,
                      private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getBadge(teamId: String?) {
        async(contextPool.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getDetailTeam(teamId)),
                        TeamResponse::class.java
                )
            }
            Picasso.get().load(data.await().teams[0].strTeamBadge).into(view)
        }
    }

}