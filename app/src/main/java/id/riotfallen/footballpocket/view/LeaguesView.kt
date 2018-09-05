package id.riotfallen.footballpocket.view

import id.riotfallen.footballpocket.model.league.League

interface LeaguesView {
    fun showLeagues(data: List<League>)
}