package id.riotfallen.footballpocket.utils

import id.riotfallen.footballpocket.api.ApiRepository

import android.widget.ImageView
import com.google.gson.Gson
import id.riotfallen.footballpocket.presenter.BadgesPresenter

class BadgeFetcher {

    private lateinit var presenter: BadgesPresenter

    fun loadBadges(id: String, img: ImageView) {
        img.setImageDrawable(null)
        val request = ApiRepository()
        val gson = Gson()

        presenter = BadgesPresenter(img, request, gson)
        presenter.getBadge(id)
    }
}
