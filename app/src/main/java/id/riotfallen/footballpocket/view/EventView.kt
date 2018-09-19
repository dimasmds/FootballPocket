package id.riotfallen.footballpocket.view

import id.riotfallen.footballpocket.model.event.Event

interface EventView {
    fun showEventLoading()
    fun hideEventLoading()
    fun showEventData(data: MutableList<Event>)
}