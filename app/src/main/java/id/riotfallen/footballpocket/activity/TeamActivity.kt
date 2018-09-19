package id.riotfallen.footballpocket.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import id.riotfallen.footballpocket.R
import id.riotfallen.footballpocket.adapter.pager.TeamPagerAdapter
import kotlinx.android.synthetic.main.activity_team.*
import org.jetbrains.anko.startActivity

class TeamActivity : AppCompatActivity() {

    private lateinit var teamPagerAdapter: TeamPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)
        setSupportActionBar(teamActivityToolbar)
        supportActionBar?.title = "List Team"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        teamActivityTabLayout.addTab(teamActivityTabLayout.newTab().setText("Teams"))
        teamActivityTabLayout.addTab(teamActivityTabLayout.newTab().setText("Saved Teams"))

        teamPagerAdapter = TeamPagerAdapter(supportFragmentManager, teamActivityTabLayout.tabCount)
        teamActivityViewPager.adapter = teamPagerAdapter
        teamActivityViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(teamActivityTabLayout))

        teamActivityTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                teamActivityViewPager.currentItem = tab.position
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_button_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_button_search -> {
                startActivity<TeamSearchActivity>()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
