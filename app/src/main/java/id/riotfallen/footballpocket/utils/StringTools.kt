package id.riotfallen.footballpocket.utils

import java.text.SimpleDateFormat
import java.util.*

class StringTools {

    fun dateToString(stringDate: String?): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(stringDate)
        return SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.getDefault())
                .format(date).toString()
    }
}