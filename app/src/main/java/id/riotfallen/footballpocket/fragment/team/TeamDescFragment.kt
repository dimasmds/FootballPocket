package id.riotfallen.footballpocket.fragment.team


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.api.ApiRepository
import id.riotfallen.footballpocket.model.team.Team
import id.riotfallen.footballpocket.presenter.TeamPresenter
import id.riotfallen.footballpocket.utils.invisible
import id.riotfallen.footballpocket.utils.visible
import id.riotfallen.footballpocket.view.TeamView

class TeamDescFragment : Fragment(), TeamView {

    companion object {
        fun newInstance(): TeamDescFragment {
            return TeamDescFragment()
        }
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var textViewDesc: TextView

    private lateinit var presenter: TeamPresenter
    private lateinit var teamId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_team_desc, container, false)
        progressBar = rootView.findViewById(R.id.teamDescFragmentProgressBar)
        textViewDesc = rootView.findViewById(R.id.teamDescFragmentTextView)

        teamId = arguments?.getString("teamId") ?: "0"

        val gson = Gson()
        val apiRepository = ApiRepository()
        presenter = TeamPresenter(this, apiRepository, gson)
        presenter.getTeamDetail(teamId)

        return rootView
    }

    override fun showTeamLoading() {
        progressBar.visible()
        textViewDesc.invisible()
    }

    override fun hideTeamLoading() {
        progressBar.invisible()
        textViewDesc.visible()
    }

    override fun showTeamData(data: List<Team>) {
        textViewDesc.text = data[0].strDescriptionEN
    }
}
