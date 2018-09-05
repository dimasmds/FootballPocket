package id.riotfallen.footballpocket.view

import id.riotfallen.footballpocket.model.event.Event

interface EventView {
    fun showLoading()
    fun hideLoading()
    fun showEvent(data: List<Event>)
}