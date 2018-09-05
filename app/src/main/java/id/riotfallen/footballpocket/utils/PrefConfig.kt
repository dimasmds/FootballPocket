package id.riotfallen.footballpocket.utils

import android.content.Context
import android.content.SharedPreferences
import id.riotfallen.footballpocket.R

class PrefConfig(val contex : Context,
                 val sharedPreferences: SharedPreferences = contex.getSharedPreferences(contex.getString(R.string.pref_file),
                         Context.MODE_PRIVATE)) {

    fun writeIdLeague(id: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(contex.getString(R.string.pref_id_league), id)
        editor.apply()
    }

    fun readIdLeague() : String = sharedPreferences.getString(contex.getString(R.string.pref_id_league), "4328")

    fun writeIdTeam(id: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(contex.getString(R.string.pref_id_team), id)
        editor.apply()
    }

    fun readIdTeam() : String = sharedPreferences.getString(contex.getString(R.string.pref_id_team), "133610")
}