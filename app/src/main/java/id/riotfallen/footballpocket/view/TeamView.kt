package id.riotfallen.footballpocket.view

import id.riotfallen.footballpocket.model.team.Team

interface TeamView {
    fun showTeamLoading()
    fun hideTeamLoading()
    fun showTeamData(data: List<Team>)
}