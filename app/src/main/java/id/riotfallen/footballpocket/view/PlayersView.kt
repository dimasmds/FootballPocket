package id.riotfallen.footballpocket.view

import id.riotfallen.footballpocket.model.player.Player

interface PlayersView {
    fun showPlayerLoading()
    fun hidePlayerLoading()
    fun showPlayerData(data: List<Player>)
}