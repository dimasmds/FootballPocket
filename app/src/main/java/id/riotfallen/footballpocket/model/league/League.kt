package id.riotfallen.footballpocket.model.league

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class League (

        @SerializedName("idLeague")
        @Expose
        var idLeague: String? = null,

        @SerializedName("strLeague")
        @Expose
        var strLeague: String? = null,

        @SerializedName("strSport")
        @Expose
        var strSport: String? = null,

        @SerializedName("strLeagueAlternate")
        @Expose
        var strLeagueAlternate: String? = null
)