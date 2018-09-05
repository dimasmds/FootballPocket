package id.riotfallen.footballpocket.presenter

import com.google.gson.Gson
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.api.TheSportDBApi
import id.riotfallen.footballpocket.model.event.EventResponse
import id.riotfallen.footballpocket.utils.CoroutineContextProvider
import id.riotfallen.footballpocket.view.EventView
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class EventDetailPresenter(private val view: EventView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson,
                           private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getEventDetail(eventId: String?){
        view.showLoading()
        async(contextPool.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi
                                .getEventDetail(eventId)),
                        EventResponse::class.java
                )
            }
            view.showEvent(data.await().events)
            view.hideLoading()
        }
    }
}