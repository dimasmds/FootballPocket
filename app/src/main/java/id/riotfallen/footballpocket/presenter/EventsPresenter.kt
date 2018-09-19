package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.event.EventResponse
import id.riotfallen.footballpocket.model.event.SearchEventResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.EventView
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg

class EventsPresenter(private val view: EventView,
                      private val apiRepository: ApiRepository,
                      private val gson: Gson,
                      private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getNextEventList(leagueId: String?) {
        view.showEventLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getNextMatch(leagueId)),
                    EventResponse::class.java
            )
        }
        launch(contextPool.main) {
            view.showEventData(data.await().events)
            view.hideEventLoading()
        }

    }

    fun getPrevEventList(leagueId: String?) {
        view.showEventLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getPrevMatch(leagueId)),
                    EventResponse::class.java
            )
        }
        launch(contextPool.main) {

            view.showEventData(data.await().events)
            view.hideEventLoading()
        }

    }

    fun searchEvent(keyword: String?) {
        view.showEventLoading()
        val data = bg {
            gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.searchEvent(keyword)),
                    SearchEventResponse::class.java)
        }
        launch(contextPool.main) {
            view.showEventData(data.await().event)
            view.hideEventLoading()

        }

    }

}